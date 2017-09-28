package com.capgemini.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.capgemini.dao.IPersonRepository;
import com.capgemini.entity.Person;

public class PersonWriterToDataBase implements ItemWriter<Person> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IPersonRepository iPersonRepository;

    public void write(List<? extends Person> items) throws Exception {
        for (Person person : items) {

            try {
                iPersonRepository.save(person);
            }
            catch (Exception exception) {

                log.error("Can't save Person [" + person.toJSON() + "], due to : " + exception.getMessage());

                throw exception;
            }
        }
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
