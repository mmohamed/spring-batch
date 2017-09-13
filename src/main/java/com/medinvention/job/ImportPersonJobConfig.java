
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
import com.medinvention.processor.ImportPersonItemProcessor;
import com.medinvention.reader.PersonReaderFromFile;
import com.medinvention.writer.PersonWriterToDataBase;

@Configuration
@EnableBatchProcessing
public class ImportPersonJobConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<Person> reader() {
        return new PersonReaderFromFile();
    }

    @Bean
    public ImportPersonItemProcessor processor() {
        return new ImportPersonItemProcessor();
    }

    @Bean
    public ItemWriter<Person> writer() {
        return new PersonWriterToDataBase();
    }

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).flow(stepImport()).end()
                .build();
    }

    @Bean
    public Step stepImport() {
        return stepBuilderFactory.get("step").<Person, Person>chunk(10).reader(reader()).processor(processor())
                .writer(writer()).build();
    }
}
