package com.mmc.reviewer.service;

import com.mmc.reviewer.api.ReviewerService;
import com.mmc.reviewer.api.domain.ReviewerTask;
import com.mmc.reviewer.mapper.ReviewerTaskMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(
        version = "1.0.0",
        interfaceClass = ReviewerService.class,
        cluster = "failfast",
        loadbalance = "roundrobin"
)
public class ReviewerServiceImpl implements ReviewerService {

    @Autowired
    private ReviewerTaskMapper reviewerTaskMapper;

    @Override
    public void addReviewerTask(ReviewerTask reviewerTask) {
        reviewerTaskMapper.insert(reviewerTask);
    }

    @Override
    public List<Long> selectReviewers(Long reportTaskId) {

        List<Long> reviewerIds=new ArrayList<>();
        reviewerIds.add(1L);
        reviewerIds.add(2L);
        reviewerIds.add(3L);

        // 每个评审员把任务插入数据库
        for (Long reviewerId:reviewerIds){
            ReviewerTask reviewerTask=new ReviewerTask();
            reviewerTask.setReviewerId(reviewerId);
            reviewerTask.setReportTaskId(reportTaskId);
            reviewerTask.setCreateTime(new Date());
            reviewerTask.setStatus(ReviewerTask.STATUS_PROCESSING);
            reviewerTaskMapper.insert(reviewerTask);
        }
        return reviewerIds;
    }

    @Override
    public void finishVote(Long reviewerId, Long reportTaskId) {
        ReviewerTask reviewerTask=new ReviewerTask();
        reviewerTask.setStatus(ReviewerTask.STATUS_FINISH);
        reviewerTask.setReviewerId(reviewerId);
        reviewerTask.setReportTaskId(reportTaskId);
        reviewerTaskMapper.update(reviewerTask);
    }
}
