package com.medinvention.reader;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import com.medinvention.dao.IPersonRepository;
import com.medinvention.dao.Person;

public class PersonReaderFromDataBase implements ItemReader<Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonReaderFromDataBase.class);

    @Autowired
    private IPersonRepository iPersonRepository;

    private Iterator<Person> cursor;

    public PersonReaderFromDataBase() {
        this.cursor = this.iPersonRepository.findAll().iterator();
    }

    public Person read() throws Exception {
        Person person = this.cursor.next();
        if (null == person) {
            LOGGER.info("All users readed from DB !");
        }
        return person;
    }
}
