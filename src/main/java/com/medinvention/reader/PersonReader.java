package com.medinvention.reader;

import org.medinvention.wrapper.PersonFieldSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

import com.medinvention.dao.Person;

public class PersonReader implements ItemReader<Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonReader.class);

    private FlatFileItemReader<Person> reader;

    public PersonReader() {
        initialize();
    }

    private void initialize() {

        this.reader = new FlatFileItemReader<Person>();
        this.reader.setResource(new ClassPathResource("sample-data.csv"));

        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<Person>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] { "firstName", "lastName" });

        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<Person>();
        fieldSetMapper.setTargetType(Person.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new PersonFieldSetMapper());

        reader.setLineMapper(lineMapper);

        reader.open(new ExecutionContext());
    }

    public Person read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        LOGGER.info("Reading the information of the next person");

        Person person = this.reader.read();

        if (null == person) {
            return null;
        }

        LOGGER.info("Found person: {}", person);

        return person;
    }
}
