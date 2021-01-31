package com.mmc.report.service.impl;

import com.mmc.report.domain.ReportTask;
import com.mmc.report.domain.ReportTaskVote;
import com.mmc.report.mapper.ReportTaskMapper;
import com.mmc.report.mapper.ReportTaskVoteMapper;
import com.mmc.report.service.ReportTaskService;
import com.mmc.report.service.ReportTaskVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReportTaskVoteServiceImpl implements ReportTaskVoteService {


    @Autowired
    private ReportTaskVoteMapper reportTaskVoteMapper;

    @Autowired
    private ReportTaskMapper reportTaskMapper;

    @Override
    public void initVotes(List<Long> reviewerIds, Long reportTaskId) {
        for (Long reviewerId:reviewerIds){
            ReportTaskVote reportTaskVote=new ReportTaskVote();
            reportTaskVote.setReportTaskId(reportTaskId);
            reportTaskVote.setReviewerId(reviewerId);
            reportTaskVote.setVoteResult(ReportTaskVote.UNKOWN);
            reportTaskVote.setCreateTime(new Date());
            reportTaskVoteMapper.insert(reportTaskVote);
        }
    }

    @Override
    public void update(ReportTaskVote reportTaskVote) {
        reportTaskVoteMapper.update(reportTaskVote);
    }

    @Override
    public boolean calculateVotes(Long reportTaskId) {
        List<ReportTaskVote> reportTaskVotes = reportTaskVoteMapper.queryByReportTaskId(reportTaskId);
        Integer quorum=reportTaskVotes.size()/2+1;

        int approvedVotes=0;
        int unapprovedVotes=0;
        for (ReportTaskVote reportTaskVote:reportTaskVotes){
            if(ReportTaskVote.APPROVED.equals(reportTaskVote.getVoteResult())){
                approvedVotes++;
            }else if(ReportTaskVote.UNAPPROVED.equals(reportTaskVote.getVoteResult())){
                unapprovedVotes++;
            }
        }

        if(approvedVotes>=quorum){
            ReportTask reportTask=new ReportTask();
            reportTask.setId(reportTaskId);
            reportTask.setVoteResult(ReportTask.VOTE_RESULT_APPROVED);
            reportTaskMapper.updateVoteResult(reportTask);
            return true;
        }else if(unapprovedVotes>=quorum){
            ReportTask reportTask=new ReportTask();
            reportTask.setId(reportTaskId);
            reportTask.setVoteResult(ReportTask.VOTE_RESULT_UNAPPROVED);
            reportTaskMapper.updateVoteResult(reportTask);
            return true;
        }
        return false;

    }

    @Override
    public List<ReportTaskVote> queryByReprtTaskId(Long reportTaskId) {
        return reportTaskVoteMapper.queryByReportTaskId(reportTaskId);
    }
}
