package com.capgemini.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.capgemini.entity.Person;
import com.capgemini.writer.PersonWriterToFile;

@Component
public class ExportJobExecutionListener implements JobExecutionListener {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    ItemWriter<Person> writer;

    public ExportJobExecutionListener(@Qualifier("exportWriter") ItemWriter<Person> writer) {
        this.writer = writer;
    }

    public void beforeJob(JobExecution jobExecution) {

        String filename = "csv/output/" + jobExecution.getJobParameters().getString("filename");

        ((PersonWriterToFile) writer).initialize(filename);

        log.info("ExportWriter initialized");
    }

    public void afterJob(JobExecution jobExecution) {
        ((PersonWriterToFile) writer).flush();

        log.info("ExportWriter flushed");
    }
}
