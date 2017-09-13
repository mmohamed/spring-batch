
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
import com.medinvention.processor.PersonItemProcessor;
import com.medinvention.reader.PersonReader;
import com.medinvention.writer.PersonWriter;

@Configuration
@EnableBatchProcessing
public class LoadPersonJobConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<Person> reader() {
        return new PersonReader();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public ItemWriter<Person> writer() {
        return new PersonWriter();
    }

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).flow(step()).end().build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step").<Person, Person>chunk(10).reader(reader()).processor(processor())
                .writer(writer()).build();
    }
}
