package com.mmc.report.service;

import com.mmc.report.domain.ReportTask;

public interface ReportTaskService {

    void addReportTask(ReportTask reportTask);

    ReportTask queryById(Long id);

    void updateReportTask(ReportTask reportTask);
}
