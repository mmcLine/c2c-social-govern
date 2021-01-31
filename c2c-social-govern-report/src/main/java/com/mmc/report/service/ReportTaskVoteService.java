package com.mmc.report.service;

import com.mmc.report.domain.ReportTaskVote;

import java.util.List;

public interface ReportTaskVoteService {

    void initVotes(List<Long> reviewerIds,Long reportTaskId);

    void update(ReportTaskVote reportTaskVote);

    boolean calculateVotes(Long reportTaskId);

    List<ReportTaskVote> queryByReprtTaskId(Long reportTaskId);
}
