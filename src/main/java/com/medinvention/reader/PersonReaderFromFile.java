package com.medinvention.reader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

import com.medinvention.dao.Person;
import com.medinvention.wrapper.PersonFieldSetMapper;

public class PersonReaderFromFile implements ItemReader<Person> {

    private FlatFileItemReader<Person> reader;

    public PersonReaderFromFile() {
        initialize();
    }

    private void initialize() {
        this.reader = new FlatFileItemReader<Person>();
        this.reader.setResource(new FileSystemResource("sample-data.csv"));

        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<Person>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] { "reference", "firstName", "lastName", "registrationDate" });

        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<Person>();
        fieldSetMapper.setTargetType(Person.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new PersonFieldSetMapper());

        reader.setLineMapper(lineMapper);

        reader.open(new ExecutionContext());
    }

    public Person read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        Person person = this.reader.read();

        if (null == person) {
            return null;
        }

        return person;
    }
}
