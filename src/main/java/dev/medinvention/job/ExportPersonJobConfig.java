
package dev.medinvention.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.medinvention.entity.Person;
import dev.medinvention.listener.ExportJobExecutionListener;
import dev.medinvention.processor.ExportPersonItemProcessor;
import dev.medinvention.reader.PersonReaderFromDataBase;
import dev.medinvention.writer.PersonWriterToFile;

@Configuration
@EnableBatchProcessing
public class ExportPersonJobConfig {

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
            @Qualifier("exportProcessor") ExportPersonItemProcessor processor, JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory) {
        return jobBuilderFactory.get("exportUserJob").incrementer(new RunIdIncrementer())
                .flow(stepExport(reader, writer, processor, stepBuilderFactory)).end()
                .listener(new ExportJobExecutionListener(writer)).build();
    }

    @Bean(name = "stepExport")
    public Step stepExport(@Qualifier("exportReader") ItemReader<Person> reader,
            @Qualifier("exportWriter") ItemWriter<Person> writer,
            @Qualifier("exportProcessor") ExportPersonItemProcessor processor, StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("stepExport").<Person, Person> chunk(10).reader(reader).processor(processor)
                .writer(writer).build();
    }
}
