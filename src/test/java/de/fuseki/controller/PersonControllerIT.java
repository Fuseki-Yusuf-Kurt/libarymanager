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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        PersonDto person1 = PersonDto.builder()
                .id(1)
                .name("yasin")
                .surName("tulyu")
                .personType(PersonType.CLIENT)
                .email("yasin.tuylu001@stud.fh-dortmund.de")
                .address(new Address("karlstr", "33", "essen", "45666"))
                .birthDate(LocalDate.parse("2004-12-28"))
                .build();

        PersonDto person2 = PersonDto.builder()
                .id(2)
                .name("yusuf")
                .surName("kurt")
                .personType(PersonType.EMPLOYEE)
                .email("yusuf.kurt001@stud.fh-dortmund.de")
                .address(new Address("jobststr", "64", "herne", "44627"))
                .birthDate(LocalDate.parse("2003-12-28"))
                .build();

        PersonDto person3 = PersonDto.builder()
                .id(3)
                .name("marc")
                .surName("fischer")
                .personType(PersonType.ADMIN)
                .email("mfischer@fuseki.de")
                .address(new Address("schürrmannstr", "32", "essen", "47434"))
                .birthDate(LocalDate.parse("1990-11-12"))
                .build();

        List<PersonDto> expected = List.of(person1, person2, person3);

        ResponseEntity<List<PersonDto>> actualResponseEntity = personController.getAllPersons();
        List<PersonDto> actual = actualResponseEntity.getBody();
        assertEquals(HttpStatusCode.valueOf(200), actualResponseEntity.getStatusCode());
        assertEquals(3, actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    @Transactional
    @Sql("/3-test-persons.sql")
    public void deletePersonTest() {
        //Given
        List<PersonDto> allPersonsBeforeDeleting = personController.getAllPersons().getBody();
        assertThat(allPersonsBeforeDeleting).hasSize(3);
        PersonDto firstPersonDto = allPersonsBeforeDeleting.getFirst();

        //When
        ResponseEntity actualResponseEntity = personController.deletePerson(firstPersonDto.getId());
        //Then
        List<PersonDto> allPersonsAfterDelete = personController.getAllPersons().getBody();

        assertEquals(HttpStatusCode.valueOf(204), actualResponseEntity.getStatusCode());
        assertEquals(2, allPersonsAfterDelete.size());
        assertFalse(allPersonsAfterDelete.contains(firstPersonDto));
    }

    @Test
    @Transactional
    public void addPersonTest() {
        //Given
        PersonDto personDto = new PersonDto();
        personDto.setId(null);
        personDto.setName("yasin");
        personDto.setSurName("tulyu");
        personDto.setPersonType(PersonType.CLIENT);
        personDto.setEmail("yasin.tuylu001@stud.fh-dortmund.de");
        Address address = new Address("karlstr", "33", "essen", "45666");
        personDto.setAddress(address);
        personDto.setBirthDate(LocalDate.parse("2004-12-28"));

        Person person = new Person();
        person.setId(1);
        person.setName("yasin");
        person.setSurName("tulyu");
        person.setPersonType(PersonType.CLIENT);
        person.setEmail("yasin.tuylu001@stud.fh-dortmund.de");
        person.setAddress(address);
        person.setBirthDate(LocalDate.parse("2004-12-28"));
        //When
        ResponseEntity<PersonDto> responseEntity = personController.addPerson(personDto);
        PersonDto returnedDto = responseEntity.getBody();
        //Then
        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(person.getPersonType(), returnedDto.getPersonType());
        assertEquals(person.getId(), returnedDto.getId());
        assertEquals(person.getEmail(), returnedDto.getEmail());
        assertEquals(person.getName(), returnedDto.getName());
        assertEquals(person.getSurName(), returnedDto.getSurName());
        assertEquals(person.getAddress(), returnedDto.getAddress());
        assertEquals(person.getBirthDate(), returnedDto.getBirthDate());
    }

    @Test
    @Transactional
    public void addPersonTestThrowsException() {
        //Given
        PersonDto personDto = new PersonDto();
        personDto.setId(1);
        personDto.setName("yasin");
        personDto.setSurName("tulyu");
        personDto.setPersonType(PersonType.CLIENT);
        personDto.setEmail("yasin.tuylu001@stud.fh-dortmund.de");
        Address address = new Address("karlstr", "33", "essen", "45666");
        personDto.setAddress(address);
        personDto.setBirthDate(LocalDate.parse("2004-12-28"));
        //When
        //Then
        assertThrowsExactly(IdShouldBeNullException.class, () -> personController.addPerson(personDto));
    }

    @Test
    @Transactional
    public void getOnePersonTest() {
        Address address = new Address("karlstr", "33", "essen", "45666");

        Person person = new Person();
        person.setId(1);
        person.setName("yasin");
        person.setSurName("tulyu");
        person.setPersonType(PersonType.CLIENT);
        person.setEmail("yasin.tuylu001@stud.fh-dortmund.de");
        person.setAddress(address);
        person.setBirthDate(LocalDate.parse("2004-12-28"));

        PersonDto personDto = new PersonDto();
        personDto.setId(null);
        personDto.setName("yasin");
        personDto.setSurName("tulyu");
        personDto.setPersonType(PersonType.CLIENT);
        personDto.setEmail("yasin.tuylu001@stud.fh-dortmund.de");
        personDto.setAddress(address);
        personDto.setBirthDate(LocalDate.parse("2004-12-28"));

        personController.addPerson(personDto);
        ResponseEntity<PersonDto> responseEntity = personController.getPerson(person.getId());

        PersonDto foundPersonDto = responseEntity.getBody();

        assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
        assertEquals(person.getPersonType(), foundPersonDto.getPersonType());
        assertEquals(person.getId(), foundPersonDto.getId());
        assertEquals(person.getEmail(), foundPersonDto.getEmail());
        assertEquals(person.getName(), foundPersonDto.getName());
        assertEquals(person.getSurName(), foundPersonDto.getSurName());
        assertEquals(person.getAddress(), foundPersonDto.getAddress());
        assertEquals(person.getBirthDate(), foundPersonDto.getBirthDate());
    }

}
