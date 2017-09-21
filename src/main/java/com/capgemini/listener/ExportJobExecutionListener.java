package com.capgemini.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.capgemini.dao.Person;
import com.capgemini.writer.PersonWriterToFile;

@Component
public class ExportJobExecutionListener implements JobExecutionListener {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    ItemWriter<Person> writer;

    public ExportJobExecutionListener(@Qualifier("exportWriter") ItemWriter<Person> writer) {
        this.writer = writer;
    }

    public void beforeJob(JobExecution jobExecution) {

        ((PersonWriterToFile) writer).initialize();

        log.info("ExportWriter initialized");
    }

    public void afterJob(JobExecution jobExecution) {
        String filename = jobExecution.getJobParameters().getString("filename");

        ((PersonWriterToFile) writer).flush(filename);

        log.info("ExportWriter flushed");
    }
}
