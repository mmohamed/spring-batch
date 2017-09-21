package com.capgemini.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.capgemini.dao.Person;
import com.capgemini.reader.PersonReaderFromFile;

@Component
public class ImportJobExecutionListener implements JobExecutionListener {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("importReader")
    ItemReader<Person> reader;

    public void beforeJob(JobExecution jobExecution) {
        String filename = jobExecution.getJobParameters().getString("filename");

        ((PersonReaderFromFile) reader).initialize(filename);
        log.info("ImportReader initialized");
    }

    public void afterJob(JobExecution jobExecution) {
        try {
            ((PersonReaderFromFile) reader).destroy();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info("ImportReader destroyed");
    }
}
