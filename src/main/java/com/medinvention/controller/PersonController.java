package com.medinvention.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
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
    @Qualifier("restorUserJob")
    Job job;

    @RequestMapping(path = "/person/run", method = RequestMethod.GET)
    public String run() throws Exception {
        jobLauncher.run(job, new JobParameters());
        return "OK runned with success! ";
    }
}
