package com.medinvention.wrapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.medinvention.dao.Person;

public class PersonFieldSetMapper implements FieldSetMapper<Person> {

    public Person mapFieldSet(FieldSet fieldSet) throws BindException {

        Person person = new Person();

        person.setFirstName(fieldSet.readString("firstName"));
        person.setLastName(fieldSet.readString("lastName"));

        return person;
    }

}
