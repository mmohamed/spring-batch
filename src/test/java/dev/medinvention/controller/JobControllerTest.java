package dev.medinvention.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.test.mock.mockito.MockBean;

import dev.medinvention.job.report.ExecutionReport;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JobControllerTest extends AbstractControllerTest {

    @MockBean
    JobLauncher jobLauncher;

    @Test
    public void testImportValidation() throws Exception {

        // required file
        this.mvc.perform(get("/person/import")).andExpect(status().isBadRequest());

        // invalid file type
        this.mvc.perform(get("/person/import?filename=abc")).andExpect(status().isBadRequest())
                .andExpect(content().contentType(TEXT_PLAIN_UTF8))
                .andExpect(content().string("[BadRequest] File with name [abc] not found !"));
        // file not found
        this.mvc.perform(get("/person/import?filename=abc.csv")).andExpect(status().isBadRequest())
                .andExpect(content().contentType(TEXT_PLAIN_UTF8))
                .andExpect(content().string("[BadRequest] File with name [abc.csv] not found !"));
    }

    @Test
    public void testExportValidation() throws Exception {
        // required file
        this.mvc.perform(get("/person/export")).andExpect(status().isBadRequest());

        // invalid file type
        this.mvc.perform(get("/person/export?filename=abc")).andExpect(status().isBadRequest())
                .andExpect(content().contentType(TEXT_PLAIN_UTF8))
                .andExpect(content().string("[BadRequest] File with name [abc] not validate (not json) !"));
    }

    @Test
    public void testImportExportValidation() throws Exception {

        // required file
        this.mvc.perform(get("/person/transform")).andExpect(status().isBadRequest());

        // invalid file type
        this.mvc.perform(get("/person/transform?filename=abc")).andExpect(status().isBadRequest())
                .andExpect(content().contentType(TEXT_PLAIN_UTF8))
                .andExpect(content().string("[BadRequest] File with name [abc] not found !"));
        // file not found
        this.mvc.perform(get("/person/transform?filename=abc.csv")).andExpect(status().isBadRequest())
                .andExpect(content().contentType(TEXT_PLAIN_UTF8))
                .andExpect(content().string("[BadRequest] File with name [abc.csv] not found !"));
    }

    @Test
    public void testSimpleImport() throws Exception {
        JobParameters parameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
                .addString("filename", "sample-data.csv").toJobParameters();
        JobInstance jobInstance = new JobInstance(1L, "importUserJob");

        JobExecution execution = new JobExecution(1L, parameters);

        execution.setStatus(BatchStatus.COMPLETED);
        execution.setStartTime(new Date());
        execution.setEndTime(new Date());
        execution.setJobInstance(jobInstance);

        Mockito.when(jobLauncher.run(Mockito.any(Job.class), Mockito.any(JobParameters.class))).thenReturn(execution);

        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(ExecutionReport.createFromJobExecution(execution));

        this.mvc.perform(get("/person/import?filename=sample-data.csv")).andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    public void testSimpleExport() throws Exception {
        JobParameters parameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
                .addString("filename", "sample-data.json").toJobParameters();
        JobInstance jobInstance = new JobInstance(1L, "exportUserJob");

        JobExecution execution = new JobExecution(1L, parameters);

        execution.setStatus(BatchStatus.COMPLETED);
        execution.setStartTime(new Date());
        execution.setEndTime(new Date());
        execution.setJobInstance(jobInstance);

        Mockito.when(jobLauncher.run(Mockito.any(Job.class), Mockito.any(JobParameters.class))).thenReturn(execution);

        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(ExecutionReport.createFromJobExecution(execution));

        this.mvc.perform(get("/person/export?filename=sample-data.json")).andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    public void testGetJobStatusNotFound() throws Exception {
        // test not found
        this.mvc.perform(get("/job/status?id=-1")).andExpect(status().isNotFound())
                .andExpect(content().contentType(TEXT_PLAIN_UTF8))
                .andExpect(content().string("[NotFound] Job with ID [-1] not found !"));
    }
}
