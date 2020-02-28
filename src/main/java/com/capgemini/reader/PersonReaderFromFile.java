package com.capgemini.reader;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

import com.capgemini.entity.Person;
import com.capgemini.wrapper.PersonFieldSetMapper;

public class PersonReaderFromFile implements ItemReader<Person> {

    private Logger log = LoggerFactory.getLogger(getClass());

    private FlatFileItemReader<Person> reader = null;

    private Boolean autoInitialized;

    private FileSystemResource file;

    public PersonReaderFromFile() {
        autoInitialized = false;
    }

    public PersonReaderFromFile(String filename) {
        autoInitialized = true;
        initialize(filename);
    }

    public void initialize(String filename) {
        reader = new FlatFileItemReader<>();
        file = new FileSystemResource(filename);
        reader.setResource(file);

        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer
                .setNames(new String[] { "registrationNumber", "firstName", "lastName", "salary", "registrationDate" });

        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Person.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new PersonFieldSetMapper());

        reader.setLineMapper(lineMapper);

        reader.open(new ExecutionContext());
    }

    public Person read() throws Exception {
        if (null == reader) {
            throw new FileReaderException();
        }

        Person person = reader.read();

        if (null == person && autoInitialized) {
            destroy();

            Files.delete(Paths.get(file.getPath()));

            log.info("{} readed and deleted!", file.getFile());
        }

        return person;
    }

    public void destroy() {
        if (null != reader) {
            reader.close();
        }
        reader = null;
    }

}
