package com.capgemini.job.report;

import java.util.Date;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ExecutionReport {

    private Long id;
    private String status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date startedAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date finishedAt;
    private JobInstanceReport jobInstanceReport;
    private Boolean isRunning;
    private Double timer;
    private String type;

    public ExecutionReport(JobInstanceReport jobInstanceReport, Long id, BatchStatus status, Date startTime,
            Date endTime, String type) {
        this.jobInstanceReport = jobInstanceReport;
        this.id = id;
        this.status = status.toString();
        this.startedAt = startTime;
        this.finishedAt = endTime;
        this.isRunning = status.isRunning();
        this.type = type;

        if (null == startTime) {
            this.timer = 0D;
        }
        else {
            this.timer = (double) ((null == endTime ? new Date().getTime() : endTime.getTime()) - startTime.getTime())
                    / 1000;
        }

    }

    public static ExecutionReport createFromJobExecution(JobExecution jobExecution) {
        String type = jobExecution.getJobParameters().getString("type", "NORMAL");

        return new ExecutionReport(JobInstanceReport.createFromJobInstance(jobExecution.getJobInstance()),
                jobExecution.getId(), jobExecution.getStatus(), jobExecution.getStartTime(), jobExecution.getEndTime(),
                type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public JobInstanceReport getJobInstanceReport() {
        return jobInstanceReport;
    }

    public void setJobInstanceReport(JobInstanceReport JobInstanceReport) {
        this.jobInstanceReport = JobInstanceReport;
    }

    public Boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    public Double getTimer() {
        return timer;
    }

    public void setTimer(Double timer) {
        this.timer = timer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
