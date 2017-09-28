package com.capgemini.wrapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.capgemini.entity.Person;

public class PersonFieldSetMapper implements FieldSetMapper<Person> {

    public Person mapFieldSet(FieldSet fieldSet) throws BindException {

        Person person = new Person();

        person.setFirstName(fieldSet.readString("firstName"));
        person.setLastName(fieldSet.readString("lastName"));
        person.setRegistrationNumber(fieldSet.readString("registrationNumber"));
        person.setSalary(Long.valueOf(fieldSet.readString("salary")));

        String registrationDate = fieldSet.readString("registrationDate");

        if (!registrationDate.isEmpty()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/y", Locale.FRANCE);
            LocalDate localDate = LocalDate.parse(registrationDate, formatter);
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            person.setRegistredAt(date);
        }

        return person;
    }

}
