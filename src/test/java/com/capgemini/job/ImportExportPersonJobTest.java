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
import org.springframework.test.context.junit4.SpringRunner;

import com.capgemini.dao.IPersonRepository;
import com.capgemini.main.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ImportExportPersonJobTest {

    @Autowired
    private IPersonRepository personRepository;

    @Autowired
    @Qualifier("importExportUserJob")
    Job importExportJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Before
    public void setUp() {
        personRepository.deleteAll();
    }

    @Test
    public void launchJob() throws Exception {

        JobParameters parameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
                .addString("filename", "sample-data.csv").toJobParameters();

        assertEquals(0, personRepository.count());

        JobExecution jobExecution = jobLauncher.run(importExportJob, parameters);

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

        assertEquals(1000, personRepository.count());

        File file = new File("sample-data.json");

        assertTrue(file.exists());
    }
}
