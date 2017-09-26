
package com.capgemini.dao;

import org.springframework.data.repository.CrudRepository;

import com.capgemini.entity.Person;

public interface IPersonRepository extends CrudRepository<Person, Long> {

}
