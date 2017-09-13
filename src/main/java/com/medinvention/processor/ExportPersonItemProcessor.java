
package com.medinvention.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.medinvention.dao.Person;

public class ExportPersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportPersonItemProcessor.class);

    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName().toLowerCase();
        final String lastName = person.getLastName().toLowerCase();

        final Person transformedPerson = new Person(firstName, lastName);

        LOGGER.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }
}
