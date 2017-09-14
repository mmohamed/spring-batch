package com.medinvention.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.medinvention.dao.IPersonRepository;
import com.medinvention.dao.Person;

public class PersonWriterToDataBase implements ItemWriter<Person> {

    @Autowired
    private IPersonRepository iPersonRepository;

    public void write(List<? extends Person> items) throws Exception {
        for (Person person : items) {
            iPersonRepository.save(person);
        }
    }
}
