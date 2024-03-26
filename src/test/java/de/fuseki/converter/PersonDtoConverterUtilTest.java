package de.fuseki.converter;

import de.fuseki.dtos.PersonDto;
import de.fuseki.entities.Address;
import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonDtoConverterUtilTest {

    Person person;
    PersonDto personDto;
    @BeforeEach
    void setUp() {
        person = new Person(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        personDto = new PersonDto(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
    }

    @Test
    void convertPersonToPersonDto() {
        //When
        PersonDto actual = PersonDtoConverterUtil.convertPersonToPersonDto(person);
        //Then
        assertEquals(personDto,actual);
    }
}