package com.mmc.report.service.impl;

import com.mmc.report.domain.ReportTask;
import com.mmc.report.mapper.ReportTaskMapper;
import com.mmc.report.service.ReportTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportTaskServiceImpl implements ReportTaskService {


    @Autowired
    private ReportTaskMapper reportTaskMapper;

    @Override
    public void addReportTask(ReportTask reportTask) {
        reportTaskMapper.insert(reportTask);
    }

    @Override
    public ReportTask queryById(Long id) {
        return reportTaskMapper.queryTaskById(id);
    }

    @Override
    public void updateReportTask(ReportTask reportTask) {
        reportTaskMapper.updateVoteResult(reportTask);
    }
}
