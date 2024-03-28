package de.fuseki.controller;

import de.fuseki.dtos.PersonDto;
import de.fuseki.entities.Address;
import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import de.fuseki.exceptions.IdShouldBeNullException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class PersonControllerIT {
    @Autowired
    PersonController personController;

    @BeforeEach
    void setUp() {

    }

    @Test
    @Sql("/3-test-persons.sql")
    @Transactional
    public void testGetUsersReturnsAllUsers() {
        //Given
        PersonDto person1 = new PersonDto(1,"yasin","tulyu", PersonType.CLIENT,"yasin.tuylu001@stud.fh-dortmund.de",new Address("karlstr", "33", "essen", "45666"), LocalDate.parse("2004-12-28"));
        PersonDto person2 = new PersonDto(2,"yusuf","kurt",PersonType.EMPLOYEE,"yusuf.kurt001@stud.fh-dortmund.de",new Address("jobststr", "64", "herne", "44627"),LocalDate.parse("2003-12-28"));
        PersonDto person3 = new PersonDto(3,"marc","fischer",PersonType.ADMIN,"mfischer@fuseki.de",new Address("schürrmannstr", "32", "essen" , "47434"),LocalDate.parse("1990-11-12"));
        List<PersonDto> expected = List.of(person1,person2,person3);

        List<PersonDto> actual = personController.getAllPersons();
        assertEquals(3, actual.size());
        assertEquals(expected.get(0),actual.get(0));
        assertEquals(expected.get(1),actual.get(1));
        assertEquals(expected.get(2),actual.get(2));
    }

    @Test
    @Transactional
    @Sql("/3-test-persons.sql")
    public void deletePersonTest(){
        personController.deletePerson(1);
        List<PersonDto> personDtoList = personController.getAllPersons();

        assertEquals(2,personDtoList.size());
    }
    @Test
    @Transactional
    public void addPersonTest(){
        //Given
        Person person = new Person(1,"yasin","tulyu", PersonType.CLIENT,"yasin.tuylu001@stud.fh-dortmund.de",new Address("karlstr", "33", "essen", "45666"), LocalDate.parse("2004-12-28"));
        PersonDto personDto = new PersonDto(null,"yasin","tulyu", PersonType.CLIENT,"yasin.tuylu001@stud.fh-dortmund.de",new Address("karlstr", "33", "essen", "45666"), LocalDate.parse("2004-12-28"));
        //When
        PersonDto returnedDto = personController.addPerson(personDto);
        //Then
        assertEquals(person.getPersonType(),returnedDto.getPersonType());
        assertEquals(person.getId(),returnedDto.getId());
        assertEquals(person.getEmail(),returnedDto.getEmail());
        assertEquals(person.getName(),returnedDto.getName());
        assertEquals(person.getSurName(),returnedDto.getSurName());
        assertEquals(person.getAddress(),returnedDto.getAddress());
        assertEquals(person.getBirthDate(),returnedDto.getBirthDate());
    }

    @Test
    @Transactional
    public void addPersonTestThrowsException(){
        //Given
        Person person = new Person(1,"yasin","tulyu", PersonType.CLIENT,"yasin.tuylu001@stud.fh-dortmund.de",new Address("karlstr", "33", "essen", "45666"), LocalDate.parse("2004-12-28"));
        PersonDto personDto = new PersonDto(1,"yasin","tulyu", PersonType.CLIENT,"yasin.tuylu001@stud.fh-dortmund.de",new Address("karlstr", "33", "essen", "45666"), LocalDate.parse("2004-12-28"));
        //When
        //Then
        assertThrowsExactly(IdShouldBeNullException.class, () ->personController.addPerson(personDto) );
    }
}
