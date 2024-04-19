package de.fuseki.repository;

import de.fuseki.entities.Address;
import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled // Not needed, because im testing here generated Code and for this im booting a whole Database.
@SpringBootTest
@Transactional
class PersonRepositoryTest {

    @Autowired
    private PersonRepository test;

    @Test
    void checkEmailExists() {
        //Given
        Person person = new Person(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        Person personResponse = test.save(person);
        //When
        boolean exists = test.existsByEmail(person.getEmail());
        //Then
        assertThat(exists).isTrue();
    }
    @Test
    void checkEmailNotExists() {
        //Given
        Person person = new Person(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        Person personResponse = test.save(person);
        //When
        boolean exists = test.existsByEmail("notExistingEmail@email.de");
        //Then
        assertThat(exists).isFalse();
    }


}