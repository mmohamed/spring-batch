package com.capgemini.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.capgemini.main.Application;
import com.capgemini.processor.ExportPersonItemProcessor;
import com.capgemini.tax.Tax;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Sql(scripts = "classpath:data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ExportPersonJobTest {

    @Mock
    RestTemplate restTemplate;

    @Autowired
    ExportPersonItemProcessor exportPersonItemProcessor;

    @Autowired
    @Qualifier("exportUserJob")
    Job exportJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Before
    public void setUp() {

        Mockito.when(restTemplate.postForObject(Matchers.matches("http://localhost:8080/tax/validate"),
                Matchers.isA(Tax.class), Matchers.eq(Boolean.class))).thenReturn((Boolean) true);

        exportPersonItemProcessor.setRestTemplate(restTemplate);
    }

    @Test
    public void launchJob() throws Exception {

        JobParameters parameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
                .addString("filename", "sample-data.json").toJobParameters();

        JobExecution jobExecution = jobLauncher.run(exportJob, parameters);

        assertNotNull(jobExecution);

        BatchStatus batchStatus = jobExecution.getStatus();
        assertEquals(BatchStatus.COMPLETED, batchStatus);
        assertFalse(batchStatus.isUnsuccessful());

        ExitStatus exitStatus = jobExecution.getExitStatus();
        assertEquals("COMPLETED", exitStatus.getExitCode());
        assertEquals("", exitStatus.getExitDescription());

        List<Throwable> failureExceptions = jobExecution.getFailureExceptions();
        assertNotNull(failureExceptions);
        assertTrue(failureExceptions.isEmpty());

        File file = new File("csv/output/sample-data.json");

        assertTrue(file.exists());
    }
}
