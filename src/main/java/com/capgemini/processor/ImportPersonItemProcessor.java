
package com.capgemini.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.web.client.RestTemplate;

import com.capgemini.entity.Person;
import com.capgemini.tax.Tax;

public class ImportPersonItemProcessor implements ItemProcessor<Person, Person> {

    private RestTemplate restTemplate;

    public ImportPersonItemProcessor() {
        this.restTemplate = new RestTemplate();
    }

    public Person process(final Person person) throws Exception {

        String registrationNumber = String.format("REF%017d", Integer.valueOf(person.getRegistrationNumber()));

        final Person transformedPerson = new Person();

        transformedPerson.setFirstName(person.getFirstName().toUpperCase());
        transformedPerson.setLastName(person.getLastName().toUpperCase());
        transformedPerson.setSalary(person.getSalary());
        transformedPerson.setRegistrationNumber(registrationNumber);
        transformedPerson.setRegistredAt(person.getRegistredAt());

        Tax requestedTax = new Tax(transformedPerson.getSalary(), person.getRegistredAt());

        Tax tax = this.restTemplate.postForObject("http://localhost:8080/tax/calculate", requestedTax, Tax.class);

        transformedPerson.setTax(tax.getValue());

        return transformedPerson;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
