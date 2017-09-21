
package com.capgemini.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.capgemini.dao.Person;
import com.capgemini.listener.ExportJobExecutionListener;
import com.capgemini.processor.ExportPersonItemProcessor;
import com.capgemini.reader.PersonReaderFromDataBase;
import com.capgemini.writer.PersonWriterToFile;

@Configuration
@EnableBatchProcessing
public class ExportPersonJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean(name = "exportReader")
    public ItemReader<Person> reader() {
        return new PersonReaderFromDataBase();
    }

    @Bean(name = "exportProcessor")
    public ExportPersonItemProcessor processor() {
        return new ExportPersonItemProcessor();
    }

    @Bean(name = "exportWriter")
    public ItemWriter<Person> writer() {
        return new PersonWriterToFile();
    }

    @Bean
    public Job exportUserJob(@Qualifier("exportReader") ItemReader<Person> reader,
            @Qualifier("exportWriter") ItemWriter<Person> writer,
            @Qualifier("exportProcessor") ExportPersonItemProcessor processor) {
        return jobBuilderFactory.get("exportUserJob").incrementer(new RunIdIncrementer())
                .flow(stepExport(reader, writer, processor)).end().listener(new ExportJobExecutionListener(writer))
                .build();
    }

    @Bean(name = "stepExport")
    public Step stepExport(@Qualifier("exportReader") ItemReader<Person> reader,
            @Qualifier("exportWriter") ItemWriter<Person> writer,
            @Qualifier("exportProcessor") ExportPersonItemProcessor processor) {
        return stepBuilderFactory.get("stepExport").<Person, Person>chunk(10).reader(reader).processor(processor)
                .writer(writer).build();
    }
}
