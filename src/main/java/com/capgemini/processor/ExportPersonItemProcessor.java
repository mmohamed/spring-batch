
package com.capgemini.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import com.capgemini.entity.Person;
import com.capgemini.tax.Tax;

public class ExportPersonItemProcessor implements ItemProcessor<Person, Person> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private RestTemplate restTemplate;

    public ExportPersonItemProcessor() {
        this.restTemplate = new RestTemplate();
    }

    public Person process(final Person person) throws Exception {

        final String firstName = StringUtils.capitalize(person.getFirstName().toLowerCase());
        final String lastName = StringUtils.capitalize(person.getLastName().toLowerCase());

        final Person transformedPerson = new Person(firstName, lastName);

        transformedPerson.setRegistrationNumber(person.getRegistrationNumber());
        transformedPerson.setRegistredAt(person.getRegistredAt());
        transformedPerson.setId(person.getId());
        transformedPerson.setSalary(person.getSalary());
        transformedPerson.setTax(person.getTax());

        Double rate = (double) transformedPerson.getTax() / (double) transformedPerson.getSalary();

        Tax requestedTax = new Tax(transformedPerson.getSalary(), transformedPerson.getTax(), rate,
                person.getRegistredAt());

        Boolean validation = this.restTemplate.postForObject("http://localhost:8080/tax/validate", requestedTax,
                Boolean.class);

        if (!validation) {

            log.info("Skipped person [{}] on export , caused by invalid Tax value [{}]!", transformedPerson.getId(),
                    requestedTax.getValue());

            return null;
        }

        return transformedPerson;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
