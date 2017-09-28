package com.capgemini.job.report;

import org.springframework.batch.core.JobInstance;

public class JobInstanceReport {

    private Long id;
    private long instanceId;
    private String jobName;

    public JobInstanceReport(Long id, long instanceId, String jobName) {
        this.id = id;
        this.instanceId = instanceId;
        this.jobName = jobName;
    }

    public static JobInstanceReport createFromJobInstance(JobInstance jobInstance) {
        return new JobInstanceReport(jobInstance.getId(), jobInstance.getInstanceId(), jobInstance.getJobName());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

}
