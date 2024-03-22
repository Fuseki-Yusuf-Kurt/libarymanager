package de.fuseki.repository;

import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import de.fuseki.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
@DataJdbcTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository test;

    @Test
    void checkIfEmailExists() {
        //Given
        Person person = new Person(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", "testaddress", LocalDate.parse("2000-01-01"));
        test.save(person);
        //When
        boolean exists = test.existsByEmail(person.getEmail());
        //Then
        assertThat(exists).isTrue();

    }
}