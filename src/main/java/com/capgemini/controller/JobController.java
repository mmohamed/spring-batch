package com.capgemini.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.controller.exception.BadRequestException;
import com.capgemini.controller.exception.NotFoundException;
import com.capgemini.job.report.ExecutionReport;
import com.capgemini.job.report.JobInstanceReport;

@RestController
public class JobController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobExplorer jobExplorer;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    @Qualifier("importUserJob")
    Job importJob;

    @Autowired
    @Qualifier("exportUserJob")
    Job exportJob;

    @Value("${DATA_PATH:}")
    String dataPath;

    @GetMapping(path = "/job/status")
    public ExecutionReport jobStatus(@RequestParam(value = "id") Long id) throws NotFoundException {

        JobExecution jobExecution = jobExplorer.getJobExecution(id);

        if (null == jobExecution) {
            throw new NotFoundException(String.format("Job with ID [%s] not found !", id));
        }

        return ExecutionReport.createFromJobExecution(jobExecution);
    }

    @GetMapping(value = { "/person/import", "person/async/import" })
    public ExecutionReport runImport(HttpServletRequest request, @RequestParam String filename)
            throws BadRequestException, JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {

        Boolean isAsync = request.getRequestURI().equals("/person/async/import");

        File file = new File(dataPath + "csv/input/" + filename);

        if (!file.exists() || file.isDirectory()) {
            throw new BadRequestException(String.format("File with name [%s] not found !", filename));
        }

        if (filename.length() < 5 || 0 != filename.substring(filename.length() - 4).toLowerCase().compareTo(".csv")) {
            throw new BadRequestException(String.format("File with name [%s] not validate (not csv) !", filename));
        }

        JobParameters parameters = new JobParametersBuilder()
                .addLong(JobInstanceReport.TIMESTAMP_PARAM, System.currentTimeMillis())
                .addString(JobInstanceReport.FILENAME_PARAM, filename).addString("type", isAsync ? "ASYNC" : "NORMAL")
                .toJobParameters();

        JobExecution jobResult;

        if (isAsync) {
            SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
            simpleJobLauncher.setJobRepository(jobRepository);

            SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor("async_import_user");

            simpleJobLauncher.setTaskExecutor(simpleAsyncTaskExecutor);

            jobResult = simpleJobLauncher.run(importJob, parameters);
        } else {
            jobResult = jobLauncher.run(importJob, parameters);
        }

        return ExecutionReport.createFromJobExecution(jobResult);
    }

    @GetMapping(value = { "/person/export", "/person/async/export" })
    public ExecutionReport runExport(HttpServletRequest request, @RequestParam String filename)
            throws BadRequestException, JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {

        Boolean isAsync = request.getRequestURI().equals("/person/async/export");

        if (filename.length() < 6 || 0 != filename.substring(filename.length() - 5).toLowerCase().compareTo(".json")) {
            throw new BadRequestException(String.format("File with name [%s] not validate (not json) !", filename));
        }

        JobParameters parameters = new JobParametersBuilder()
                .addLong(JobInstanceReport.TIMESTAMP_PARAM, System.currentTimeMillis())
                .addString(JobInstanceReport.FILENAME_PARAM, "sample-data.json")
                .addString("type", isAsync ? "ASYNC" : "NORMAL").toJobParameters();

        JobExecution jobResult;

        if (isAsync) {
            SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
            simpleJobLauncher.setJobRepository(jobRepository);

            SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();

            simpleJobLauncher.setTaskExecutor(simpleAsyncTaskExecutor);

            jobResult = simpleJobLauncher.run(exportJob, parameters);
        } else {
            jobResult = jobLauncher.run(exportJob, parameters);
        }

        return ExecutionReport.createFromJobExecution(jobResult);
    }

    @GetMapping("/person/transform")
    public ExecutionReport runTransform(HttpServletRequest request, @RequestParam String filename)
            throws BadRequestException, JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {

        File file = new File(dataPath + "csv/input/" + filename);

        if (!file.exists() || file.isDirectory()) {
            throw new BadRequestException(String.format("File with name [%s] not found !", filename));
        }

        if (filename.length() < 5 || 0 != filename.substring(filename.length() - 4).toLowerCase().compareTo(".csv")) {
            throw new BadRequestException(String.format("File with name [%s] not validate (not csv) !", filename));
        }

        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext("config/job.xml");
        JobLauncher asyncJobLauncher = (JobLauncher) context.getBean("asyncJobLauncher");

        Job job = (Job) context.getBean("transformJob");

        JobParameters parameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
                .addString("filename", filename).addString("type", "MULTI").toJobParameters();

        JobExecution jobResult = asyncJobLauncher.run(job, parameters);

        return ExecutionReport.createFromJobExecution(jobResult);
    }
}
