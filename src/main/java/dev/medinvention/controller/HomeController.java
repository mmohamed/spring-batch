package dev.medinvention.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import dev.medinvention.job.report.ExecutionReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class HomeController {

    @Autowired
    JobExplorer jobExplorer;

    @GetMapping("/")
    public ModelAndView toHome() {
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/home")
    public ModelAndView home() {
        String[] jobNames = new String[] { "importUserJob", "exportUserJob", "transformJob" };

        Map<String, List<ExecutionReport>> allJobExecutionReports = new HashMap<>();

        for (String jobName : jobNames) {

            List<ExecutionReport> jobExecutionReports = new ArrayList<>();

            try {

                List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0,
                        Math.min(3, jobExplorer.getJobInstanceCount(jobName)));

                for (JobInstance jobInstance : jobInstances) {

                    List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);

                    for (JobExecution jobExecution : jobExecutions) {

                        jobExecutionReports.add(ExecutionReport.createFromJobExecution(jobExecution));
                    }
                }

                jobExecutionReports.sort((firstReport, secondReport) -> firstReport.getJobInstanceReport().getId()
                        .compareTo(secondReport.getJobInstanceReport().getId()));

                allJobExecutionReports.put(jobName, jobExecutionReports);
            } catch (NoSuchJobException e) {
                // ignore it
            }
        }

        ObjectMapper mapper = new ObjectMapper();

        String jobExecutionsJSON;

        try {

            jobExecutionsJSON = mapper.writeValueAsString(allJobExecutionReports);
        } catch (JsonProcessingException e) {
            jobExecutionsJSON = "{}";
        }

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("home");
        modelAndView.addObject("jobExecutionsJSON", jobExecutionsJSON);

        return modelAndView;
    }
}