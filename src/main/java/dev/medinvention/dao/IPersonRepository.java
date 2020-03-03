
package dev.medinvention.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import dev.medinvention.entity.Person;

public interface IPersonRepository extends CrudRepository<Person, Long> {

    Page<Person> findAll(Pageable pageable);
}
