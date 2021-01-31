package com.mmc.reviewer.api.domain;

import java.util.Date;

public class ReviewerTask {

    /**
     * 处理中
     */
    public static final Integer STATUS_PROCESSING=1;

    /**
     * 处理完成
     */
    public static final Integer STATUS_FINISH=2;

    private Long id;
    private Long reviewerId;
    private Long reportTaskId;
    private Integer status;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getReportTaskId() {
        return reportTaskId;
    }

    public void setReportTaskId(Long reportTaskId) {
        this.reportTaskId = reportTaskId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
