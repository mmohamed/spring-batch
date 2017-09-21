package com.capgemini.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.capgemini.listener.ImportExportJobExecutionListener;

@Configuration
@EnableBatchProcessing
public class ImportExportJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private ImportExportJobExecutionListener importExportJobExecutionListener;

    @Bean
    public Job importExportUserJob(@Qualifier("stepImport") Step stepImport, @Qualifier("stepExport") Step stepExport) {
        return jobBuilderFactory.get("importExportUserJob").incrementer(new RunIdIncrementer()).flow(stepImport)
                .next(stepExport).end().listener(importExportJobExecutionListener).build();
    }
}
