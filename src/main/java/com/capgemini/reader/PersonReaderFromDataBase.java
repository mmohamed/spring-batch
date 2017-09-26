package com.capgemini.reader;

import java.util.Iterator;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import com.capgemini.dao.IPersonRepository;
import com.capgemini.entity.Person;

public class PersonReaderFromDataBase implements ItemReader<Person> {

    @Autowired
    private IPersonRepository iPersonRepository;

    private Iterator<Person> cursor;

    public Person read() throws Exception {
        if (null == this.cursor) {
            this.cursor = this.iPersonRepository.findAll().iterator();
        }

        if (!this.cursor.hasNext()) {
            this.cursor = null;
            return null;
        }

        return this.cursor.next();
    }

    /**
     * @return the iPersonRepository
     */
    public IPersonRepository getiPersonRepository() {
        return iPersonRepository;
    }

    /**
     * @param iPersonRepository the iPersonRepository to set
     */
    public void setiPersonRepository(IPersonRepository iPersonRepository) {
        this.iPersonRepository = iPersonRepository;
    }
}
