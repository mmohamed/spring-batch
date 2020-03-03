package dev.medinvention.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import dev.medinvention.dao.IPersonRepository;
import dev.medinvention.main.Application;
import dev.medinvention.processor.ExportPersonItemProcessor;
import dev.medinvention.processor.ImportPersonItemProcessor;
import dev.medinvention.tax.Tax;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransformPersonJobTest {

    @Mock
    RestTemplate restTemplate;

    @Autowired
    private IPersonRepository personRepository;

    @Test
    public void launchJob() throws Exception {

        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext("config/job-test.xml");
        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");

        Job job = (Job) context.getBean("transformJob");

        ImportPersonItemProcessor importProcessor = (ImportPersonItemProcessor) context.getBean("importProcessor");
        ExportPersonItemProcessor exportProcessor = (ExportPersonItemProcessor) context.getBean("exportProcessor");

        Mockito.when(restTemplate.postForObject(Matchers.matches("http://localhost:8080/tax/calculate"),
                Matchers.isA(Tax.class), Matchers.eq(Tax.class))).thenReturn(new Tax(null, 1000L, null, null));

        Mockito.when(restTemplate.postForObject(Matchers.matches("http://localhost:8080/tax/validate"),
                Matchers.isA(Tax.class), Matchers.eq(Boolean.class))).thenReturn((Boolean) true);

        exportProcessor.setRestTemplate(restTemplate);
        importProcessor.setRestTemplate(restTemplate);

        JobParameters parameters = new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis())
                .addString("filename", "sample-data.csv").toJobParameters();

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
