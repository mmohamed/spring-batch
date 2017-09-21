package com.capgemini.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.capgemini.dao.IPersonRepository;
import com.capgemini.dao.Person;

public class PersonWriterToDataBase implements ItemWriter<Person> {

    @Autowired
    private IPersonRepository iPersonRepository;

    public void write(List<? extends Person> items) throws Exception {
        for (Person person : items) {
            iPersonRepository.save(person);
        }
    }
}
