package dev.medinvention.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import dev.medinvention.dao.IPersonRepository;
import dev.medinvention.entity.Person;

public class PersonWriterToDataBase implements ItemWriter<Person> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IPersonRepository iPersonRepository;

    public void write(List<? extends Person> items) throws Exception {

        for (Person person : items) {

            String error = null;

            try {
                iPersonRepository.save(person);
            } catch (Exception exception) {

                log.error("Can't save Person [{}], due to : {}", person.toJSON(), exception.getMessage());

                error = exception.getMessage();
            }

            if (null != error) {
                throw new PersonWriterException(error);
            }
        }
    }

    public IPersonRepository getiPersonRepository() {
        return iPersonRepository;
    }

    public void setiPersonRepository(IPersonRepository iPersonRepository) {
        this.iPersonRepository = iPersonRepository;
    }
}
