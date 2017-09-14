package com.medinvention.reader;

import java.util.Iterator;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import com.medinvention.dao.IPersonRepository;
import com.medinvention.dao.Person;

public class PersonReaderFromDataBase implements ItemReader<Person> {

    @Autowired
    private IPersonRepository iPersonRepository;

    private Iterator<Person> cursor;

    public Person read() throws Exception {
        if (null == this.cursor) {
            this.cursor = this.iPersonRepository.findAll().iterator();
        }

        if (!this.cursor.hasNext()) {
            return null;
        }

        return this.cursor.next();
    }
}
