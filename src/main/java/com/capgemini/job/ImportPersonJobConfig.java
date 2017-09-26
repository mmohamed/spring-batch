
package com.capgemini.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.capgemini.entity.Person;
import com.capgemini.listener.ImportJobExecutionListener;
import com.capgemini.processor.ImportPersonItemProcessor;
import com.capgemini.reader.PersonReaderFromFile;
import com.capgemini.tasklet.CleanDBTasklet;
import com.capgemini.validator.FileNameParameterValidator;
import com.capgemini.writer.PersonWriterToDataBase;

@Configuration
@EnableBatchProcessing
public class ImportPersonJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean(name = "importReader")
    public ItemReader<Person> reader() {
        return new PersonReaderFromFile();
    }

    @Bean(name = "importProcessor")
    public ImportPersonItemProcessor processor() {
        return new ImportPersonItemProcessor();
    }

    @Bean(name = "importWriter")
    public ItemWriter<Person> writer() {
        return new PersonWriterToDataBase();
    }

    @Bean(name = "importCleaner")
    public Tasklet cleaner() {
        return new CleanDBTasklet();
    }

    @Bean
    public Job importUserJob(@Qualifier("importReader") ItemReader<Person> reader,
            @Qualifier("importWriter") ItemWriter<Person> writer,
            @Qualifier("importProcessor") ImportPersonItemProcessor processor) {
        return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).flow(stepClean())
                .next(stepImport(reader, writer, processor)).end().listener(new ImportJobExecutionListener(reader))
                .validator(new FileNameParameterValidator()).build();
    }

    @Bean(name = "stepClean")
    public Step stepClean() {
        return stepBuilderFactory.get("stepClean").tasklet(cleaner()).build();
    }

    @Bean(name = "stepImport")
    public Step stepImport(@Qualifier("importReader") ItemReader<Person> reader,
            @Qualifier("importWriter") ItemWriter<Person> writer,
            @Qualifier("importProcessor") ImportPersonItemProcessor processor) {
        return stepBuilderFactory.get("stepImport").<Person, Person>chunk(10).reader(reader).processor(processor)
                .writer(writer).build();
    }
}
