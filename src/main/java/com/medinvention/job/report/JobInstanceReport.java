package com.medinvention.job.report;

import org.springframework.batch.core.JobInstance;

public class JobInstanceReport {

    private Long id;
    private long instanceId;
    private String jobName;

    /**
     * Default Constructor.
     * 
     * @param id
     * @param instanceId
     * @param jobName
     */
    public JobInstanceReport(Long id, long instanceId, String jobName) {
        this.id = id;
        this.instanceId = instanceId;
        this.jobName = jobName;
    }

    public static JobInstanceReport createFromJobInstance(JobInstance jobInstance) {
        return new JobInstanceReport(jobInstance.getId(), jobInstance.getInstanceId(), jobInstance.getJobName());
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
     * @return the instanceId
     */
    public long getInstanceId() {
        return instanceId;
    }

    /**
     * @param instanceId the instanceId to set
     */
    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

}
