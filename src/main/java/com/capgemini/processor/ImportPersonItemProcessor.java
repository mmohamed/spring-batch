
package com.capgemini.processor;

import org.springframework.batch.item.ItemProcessor;

import com.capgemini.entity.Person;

public class ImportPersonItemProcessor implements ItemProcessor<Person, Person> {

    public Person process(final Person person) throws Exception {

        person.setFirstName(person.getFirstName().toUpperCase());
        person.setLastName(person.getLastName().toUpperCase());
        person.setReference(String.format("REF%017d", Integer.valueOf(person.getReference())));

        return person;
    }
}
