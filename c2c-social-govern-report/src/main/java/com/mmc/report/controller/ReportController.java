package com.mmc.report.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.druid.pool.DruidDataSource;
import com.mmc.report.domain.ReportTask;
import com.mmc.report.domain.ReportTaskVote;
import com.mmc.report.service.ReportTaskService;
import com.mmc.report.service.ReportTaskVoteService;
import com.mmc.reviewer.api.ReviewerService;
import com.mmc.reward.api.RewardService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private static final String FLOW_RESOURCE_KEY ="reviewerService";

    static {
        initFlowRules();
    }

    private static void initFlowRules(){
//        List<FlowRule> rules = new ArrayList<>();
//        FlowRule rule = new FlowRule();
//        rule.setResource(FLOW_RESOURCE_KEY);
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        // Set limit QPS to 20.
//        rule.setCount(2);
//        FlowRuleManager.loadRules(rules);
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule(FLOW_RESOURCE_KEY);
        rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
        rule.setCount(0.2);
        rule.setTimeWindow(3);
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);

    }

    /**
     * http://localhost:18000/report?type=tousu&reportUserId=1&reportContent=not%20good&targetId=2
     * @param reportTask
     * @return
     */
    @RequestMapping("/report")
    @GlobalTransactional(name = "createReport",rollbackFor = Exception.class)
    public String report(ReportTask reportTask){
        reportTask.setCreateTime(new Date());
        reportTaskService.addReportTask(reportTask);

        Entry entry = null;
        List<Long> reviewers=null;
        try {
            entry = SphU.entry(FLOW_RESOURCE_KEY);
            /*您的业务逻辑 - 开始*/
            //调用评审员服务，选择一些评审员出来
            reviewers = reviewerService.selectReviewers(reportTask.getId());
            System.out.println("服务开始");
            int i=0/0;
            /*您的业务逻辑 - 结束*/
        } catch (Throwable e1) {
            if (!BlockException.isBlockException(e1)) {
                Tracer.trace(e1);
            }
            /*流控逻辑处理 - 结束*/
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }


        if(reviewers==null){
            return "fail";
        }
        reportTaskVoteService.initVotes(reviewers,reportTask.getId());
        System.out.println("模拟发送消息给评审员....");
        return "success";
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

    @Value("${myname:aa}")
    private String myname;

    @RequestMapping("/test")
    public String test(){
        return myname;
    }



}
