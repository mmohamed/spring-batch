package com.medinvention.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("importUserJob")
    Job importJob;

    @Autowired
    @Qualifier("exportUserJob")
    Job exportJob;

    @RequestMapping(path = "/person/import", method = RequestMethod.GET)
    public String runImport() throws Exception {
        JobParameters parameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobResult = jobLauncher.run(importJob, parameters);
        return "Import Job runned with exit status [" + jobResult.getExitStatus().toString() + "]";
    }

    @RequestMapping(path = "/person/export", method = RequestMethod.GET)
    public String runExport() throws Exception {
        JobParameters parameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobResult = jobLauncher.run(exportJob, parameters);
        return "Export Job runned with exit status [" + jobResult.getExitStatus().toString() + "]";
    }
}
