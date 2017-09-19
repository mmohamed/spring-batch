package com.medinvention.job.report;

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

    /**
     * Default Constructor.
     * 
     * @param id
     * @param status
     * @param startTime
     * @param endTime
     * @param type
     */
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

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the startedAt
     */
    public Date getStartedAt() {
        return startedAt;
    }

    /**
     * @param startedAt the startedAt to set
     */
    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * @return the finishedAt
     */
    public Date getFinishedAt() {
        return finishedAt;
    }

    /**
     * @param finishedAt the finishedAt to set
     */
    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    /**
     * @return the JobInstanceReport
     */
    public JobInstanceReport getJobInstanceReport() {
        return jobInstanceReport;
    }

    /**
     * @param jobInstanceReport the JobInstanceReport to set
     */
    public void setJobInstanceReport(JobInstanceReport JobInstanceReport) {
        this.jobInstanceReport = JobInstanceReport;
    }

    /**
     * @return the isRunning
     */
    public Boolean isRunning() {
        return isRunning;
    }

    /**
     * @param isRunning the isRunning to set
     */
    public void setIsRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    /**
     * @return the timer
     */
    public Double getTimer() {
        return timer;
    }

    /**
     * @param timer the timer to set
     */
    public void setTimer(Double timer) {
        this.timer = timer;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
