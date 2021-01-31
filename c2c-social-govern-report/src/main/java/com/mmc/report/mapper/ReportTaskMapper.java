package com.mmc.report.mapper;

import com.mmc.report.domain.ReportTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ReportTaskMapper{

    void insert(ReportTask reportTask);

    ReportTask queryTaskById(Long id);

    void updateVoteResult(ReportTask reportTask);
}
