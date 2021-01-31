package com.mmc.reviewer.api;

import com.mmc.reviewer.api.domain.ReviewerTask;

import java.util.List;

/**
 * 评审员服务的接口
 */
public interface ReviewerService {

    void addReviewerTask(ReviewerTask reviewerTask);

    List<Long> selectReviewers(Long reportTaskId);

    void finishVote(Long reviewerId,Long reportTaskId);



}
