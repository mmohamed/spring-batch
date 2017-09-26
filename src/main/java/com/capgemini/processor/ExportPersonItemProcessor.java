
package com.capgemini.processor;

import org.springframework.batch.item.ItemProcessor;

import com.capgemini.entity.Person;

public class ExportPersonItemProcessor implements ItemProcessor<Person, Person> {

    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName().toLowerCase();
        final String lastName = person.getLastName().toLowerCase();

        final Person transformedPerson = new Person(firstName, lastName);

        transformedPerson.setReference(person.getReference());
        transformedPerson.setRegistredAt(person.getRegistredAt());
        transformedPerson.setId(person.getId());

        return transformedPerson;
    }
}
