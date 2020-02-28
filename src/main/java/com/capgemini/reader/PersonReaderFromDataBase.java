package com.capgemini.reader;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.capgemini.dao.IPersonRepository;
import com.capgemini.entity.Person;

public class PersonReaderFromDataBase implements ItemReader<Person> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPersonRepository iPersonRepository;

    private Iterator<Person> cursor;

    private Integer page;

    private Integer size;

    public Person read() throws Exception {
        if (null == this.cursor) {
            init();
        }

        if (!this.cursor.hasNext()) {
            this.cursor = null;
            return null;
        }

        return this.cursor.next();
    }

    private void init() {
        if (null == page || null == size) {
            this.cursor = this.iPersonRepository.findAll().iterator();
        }
        else {
            log.info("Read from DB with page {} and size {}", page, size);

            this.cursor = this.iPersonRepository.findAll(new PageRequest(page, size)).iterator();
        }
    }

    public IPersonRepository getiPersonRepository() {
        return iPersonRepository;
    }

    public void setiPersonRepository(IPersonRepository iPersonRepository) {
        this.iPersonRepository = iPersonRepository;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}
