package com.capgemini.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.UnexpectedJobExecutionException;
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
        reader = new FlatFileItemReader<Person>();
        file = new FileSystemResource(filename);
        reader.setResource(file);

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
        if (null == reader) {
            throw new Exception("FileReader don't initialized !");
        }

        Person person = reader.read();

        if (null == person && autoInitialized) {
            destroy();
            // and delete file
            boolean deleted = file.getFile().delete();
            if (!deleted) {
                throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
            }
            else {
                log.info(file.getFile() + " readed and deleted!");
            }
        }

        return person;
    }

    public void destroy() throws Exception {
        if (null != reader) {
            reader.close();
        }
        reader = null;
    }

}
