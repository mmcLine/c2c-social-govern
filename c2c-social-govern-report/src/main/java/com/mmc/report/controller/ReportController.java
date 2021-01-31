package com.mmc.report.controller;

import com.mmc.report.domain.ReportTask;
import com.mmc.report.domain.ReportTaskVote;
import com.mmc.report.service.ReportTaskService;
import com.mmc.report.service.ReportTaskVoteService;
import com.mmc.reviewer.api.ReviewerService;
import com.mmc.reward.api.RewardService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ReportController {

    @Reference(version = "1.0.0",
            interfaceClass = ReviewerService.class,
            cluster = "failfast")
    private ReviewerService reviewerService;

    @Reference(version = "1.0.0",
            interfaceClass = RewardService.class,
            cluster = "failfast")
    private RewardService rewardService;

    @Autowired
    private ReportTaskService reportTaskService;

    @Autowired
    private ReportTaskVoteService reportTaskVoteService;


    /**
     * http://localhost:8080/report?type=tousu&reportUserId=1&reportContent=not%20good&targetId=2
     * @param reportTask
     * @return
     */
    @RequestMapping("/report")
    @GlobalTransactional(name = "createReport",rollbackFor = RuntimeException.class)
    public String report(ReportTask reportTask){
        reportTask.setCreateTime(new Date());
        reportTaskService.addReportTask(reportTask);

        //调用评审员服务，选择一些评审员出来w
        List<Long> reviewers = reviewerService.selectReviewers(reportTask.getId());

        reportTaskVoteService.initVotes(reviewers,reportTask.getId());
        System.out.println("模拟发送消息给评审员....");
        throw new RuntimeException();
//        return "success";
    }


    @GetMapping("/report/{id}")
    public ReportTask queryReport(@PathVariable("id") Long id){
        return reportTaskService.queryById(id);
    }

    @RequestMapping("/report/vote")
    public String vote(ReportTaskVote reportTaskVote){
        reportTaskVoteService.update(reportTaskVote);
        reviewerService.finishVote(reportTaskVote.getReviewerId(),reportTaskVote.getReportTaskId());
        boolean voteFinish = reportTaskVoteService.calculateVotes(reportTaskVote.getReportTaskId());
        //如果投票成功，则发放奖励
        if(voteFinish){
            List<ReportTaskVote> reportTaskVotes = reportTaskVoteService.queryByReprtTaskId(reportTaskVote.getReportTaskId());
            List<Long> reviewerIds=new ArrayList<>();
            for (ReportTaskVote vote:reportTaskVotes){
                reviewerIds.add(vote.getReviewerId());
            }
            rewardService.giveReward(reviewerIds);
        }
        return "success";
    }
}
