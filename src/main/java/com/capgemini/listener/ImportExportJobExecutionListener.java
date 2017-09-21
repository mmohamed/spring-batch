package com.capgemini.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.capgemini.dao.Person;
import com.capgemini.reader.PersonReaderFromFile;
import com.capgemini.writer.PersonWriterToFile;

@Component
public class ImportExportJobExecutionListener implements JobExecutionListener {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("importReader")
    ItemReader<Person> importReader;

    @Autowired
    @Qualifier("exportWriter")
    ItemWriter<Person> exportWriter;

    public void beforeJob(JobExecution jobExecution) {
        String filename = jobExecution.getJobParameters().getString("filename");

        ((PersonReaderFromFile) importReader).initialize(filename);
        log.info("IE - ImportReader initialized");

        ((PersonWriterToFile) exportWriter).initialize();

        log.info("IE - ExportWriter initialized");
    }

    public void afterJob(JobExecution jobExecution) {
        try {
            ((PersonReaderFromFile) importReader).destroy();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info("IE - ImportReader destroyed");

        String filename = jobExecution.getJobParameters().getString("filename");

        ((PersonWriterToFile) exportWriter).flush(filename.replace(".csv", ".json"));

        log.info("IE - ExportWriter flushed");
    }
}
