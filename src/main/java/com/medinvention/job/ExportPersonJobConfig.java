
package com.medinvention.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.medinvention.dao.Person;
import com.medinvention.processor.ExportPersonItemProcessor;
import com.medinvention.reader.PersonReaderFromDataBase;
import com.medinvention.writer.PersonWriterToFile;

@Configuration
@EnableBatchProcessing
public class ExportPersonJobConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<Person> reader() {
        return new PersonReaderFromDataBase();
    }

    @Bean
    public ExportPersonItemProcessor processor() {
        return new ExportPersonItemProcessor();
    }

    @Bean
    public ItemWriter<Person> writer() {
        return new PersonWriterToFile();
    }

    @Bean
    public Job exportUserJob() {
        return jobBuilderFactory.get("exportUserJob").incrementer(new RunIdIncrementer()).flow(stepExport()).end()
                .build();
    }

    @Bean
    public Step stepExport() {
        return stepBuilderFactory.get("stepExport").<Person, Person>chunk(10).reader(reader()).processor(processor())
                .writer(writer()).build();
    }
}
