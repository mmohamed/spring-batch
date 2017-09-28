package com.capgemini.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.capgemini.job.report.ExecutionReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class HomeController {

    @Autowired
    JobExplorer jobExplorer;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home() {
        String jobNames[] = new String[] { "importUserJob", "exportUserJob", "transformJob" };

        Map<String, List<ExecutionReport>> allJobExecutionReports = new HashMap<String, List<ExecutionReport>>();

        for (String jobName : jobNames) {

            List<ExecutionReport> jobExecutionReports = new ArrayList<ExecutionReport>();

            try {

                List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0,
                        jobExplorer.getJobInstanceCount(jobName));

                for (JobInstance jobInstance : jobInstances) {

                    List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);

                    for (JobExecution jobExecution : jobExecutions) {

                        jobExecutionReports.add(ExecutionReport.createFromJobExecution(jobExecution));
                    }
                }

                jobExecutionReports.sort(new Comparator<ExecutionReport>() {
                    @Override
                    public int compare(ExecutionReport firstReport, ExecutionReport secondReport) {
                        return firstReport.getJobInstanceReport().getId()
                                .compareTo(secondReport.getJobInstanceReport().getId());
                    }
                });

                allJobExecutionReports.put(jobName, jobExecutionReports);
            }
            catch (NoSuchJobException e) {
                // ignore it
            }
        }

        ObjectMapper mapper = new ObjectMapper();

        String jobExecutionsJSON;

        try {

            jobExecutionsJSON = mapper.writeValueAsString(allJobExecutionReports);
        }
        catch (JsonProcessingException e) {
            jobExecutionsJSON = "{}";
        }

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("home");
        modelAndView.addObject("jobExecutionsJSON", jobExecutionsJSON);

        return modelAndView;
    }
}
