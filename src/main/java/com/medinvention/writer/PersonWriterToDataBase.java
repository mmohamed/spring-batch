package com.medinvention.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.medinvention.dao.IPersonRepository;
import com.medinvention.dao.Person;

public class PersonWriterToDataBase implements ItemWriter<Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonWriterToDataBase.class);

    @Autowired
    private IPersonRepository iPersonRepository;

    public void write(List<? extends Person> items) throws Exception {

        LOGGER.info("Received the information of " + items.size() + " persons");

        for (Person person : items) {

            LOGGER.info(String.format("inserting for person %s %s", person.getFirstName(), person.getLastName()));

            iPersonRepository.save(person);

            LOGGER.info(String.format("person id : %d", person.getId()));
        }

    }
}
