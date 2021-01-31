package com.mmc.report.mapper;

import com.mmc.report.domain.ReportTaskVote;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportTaskVoteMapper {

    void insert(ReportTaskVote reportTaskVote);

    void update(ReportTaskVote reportTaskVote);

    List<ReportTaskVote> queryByReportTaskId(Long reportTaskId);
}
