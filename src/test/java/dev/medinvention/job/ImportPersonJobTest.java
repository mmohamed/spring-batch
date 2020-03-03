package dev.medinvention.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import dev.medinvention.dao.IPersonRepository;
import dev.medinvention.main.Application;
import dev.medinvention.processor.ImportPersonItemProcessor;
import dev.medinvention.tax.Tax;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ImportPersonJobTest {

    @Mock
    RestTemplate restTemplate;

    @Autowired
    ImportPersonItemProcessor importPersonItemProcessor;

    @Autowired
    private IPersonRepository personRepository;

    @Autowired
    @Qualifier("importUserJob")
    Job importJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Before
    public void setUp() {

        personRepository.deleteAll();

        Tax tax = new Tax();
        tax.setValue(10000L);

        Mockito.when(restTemplate.postForObject(Matchers.matches("http://localhost:8080/tax/calculate"),
                Matchers.isA(Tax.class), Matchers.eq(Tax.class))).thenReturn(tax);

        importPersonItemProcessor.setRestTemplate(restTemplate);
    }

    @Test
    public void launchJob() throws Exception {

        JobParameters parameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
                .addString("filename", "sample-data.csv").toJobParameters();

        assertEquals(0, personRepository.count());

        JobExecution jobExecution = jobLauncher.run(importJob, parameters);

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

        parameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
                .addString("filename", "sample-data.csv").toJobParameters();

        jobExecution = jobLauncher.run(importJob, parameters);

        assertNotNull(jobExecution);

        batchStatus = jobExecution.getStatus();
        assertEquals(BatchStatus.COMPLETED, batchStatus);
        assertFalse(batchStatus.isUnsuccessful());

        assertEquals(1000, personRepository.count());
    }
}
