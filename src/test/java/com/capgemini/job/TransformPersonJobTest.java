package com.capgemini.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.capgemini.dao.IPersonRepository;
import com.capgemini.main.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransformPersonJobTest {

    @Test
    public void launchJob() throws Exception {

        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext("config/job-test.xml");
        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");

        Job job = (Job) context.getBean("transformJob");

        JobParameters parameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
                .addString("filename", "sample-data.csv").toJobParameters();

        IPersonRepository personRepository = (IPersonRepository) context.getBean("IPersonRepository");

        personRepository.deleteAll();

        assertEquals(0, personRepository.count());

        JobExecution jobExecution = jobLauncher.run(job, parameters);

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

        File file = new File("csv/output/export.json");

        assertTrue(file.exists());
    }
}
