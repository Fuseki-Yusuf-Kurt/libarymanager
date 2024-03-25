package de.fuseki.service;

import de.fuseki.entities.Address;
import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import de.fuseki.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    PersonRepository personRepository;
    @InjectMocks
    private PersonService underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void gestsAllPersons() {
        //Given
        Person person = new Person(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        //Mocking
        when(personRepository.findAll()).thenReturn(List.of(person));

        //When
        List<Person> personList = underTest.getAllPersons();
        //Then
        assertEquals(List.of(person), personList);
    }

    @Test
    void deletePerson() {
        //Mocking
        //When
        underTest.deletePerson(1);
        //Then
        verify(personRepository, times(1)).deleteById(1);
    }

    @Test
    void addNewPerson() {
        //Given
        Person person = new Person(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        //When
        underTest.addNewPerson(person);
        //Then
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void updatePersonThrowsExceptionBecouseIdDoesNotExist() {
        //Mocking
        when(personRepository.existsById(1)).thenReturn(false);
        //Then
        assertThrows(RuntimeException.class, () -> {
            underTest.updatePerson(1, null, null, null, null, null, null);
        });
    }

    @Test
    void updatePersonThrowsExceptionBecouseEmailAlreadyExists() {
        //Given
        Person person = new Person(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        Person person2 = new Person(2, "testname", "testsurname", PersonType.CLIENT, "existingmail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        //Mocking
        when(personRepository.existsById(1)).thenReturn(true);
        when(personRepository.getReferenceById(1)).thenReturn(person);
        when(personRepository.existsByEmail("existingmail@test.test")).thenReturn(true);
        //Then
        assertThrows(RuntimeException.class, () -> {
            underTest.updatePerson(1, null, null, null, "esistingmail@test.test", null, null);
        });
    }

    @Test
    void updatePersonUpdatesEveryAttributeExceptAddress() {
        //Given
        String oldName = "testname";
        String oldSurname = "testsurname";
        PersonType oldPersonType = PersonType.CLIENT;
        String oldMail = "testemail@test.test";
        Address oldAddress = new Address("test", "test", "test", "test");
        LocalDate oldDate = LocalDate.parse("2000-01-01");
        Person person = new Person(1, oldName, oldSurname, oldPersonType, oldMail, oldAddress, oldDate);

        String newName = "newname";
        String newEmail = "newemail@test.test";
        String newSurname = "newsurname";
        PersonType newPerosonType = PersonType.ADMIN;
        LocalDate newDate = LocalDate.parse("2001-01-01");
        Address newAddress = new Address("newStreet","newNumber","newCity","newPostalCode");
        //Mocking
        when(personRepository.existsById(1)).thenReturn(true);
        when(personRepository.getReferenceById(1)).thenReturn(person);
        when(personRepository.existsByEmail(newEmail)).thenReturn(false);

        //When
        underTest.updatePerson(1, newName, newSurname, newPerosonType, newEmail, newAddress, newDate);

        //Then
        assertEquals(newName, person.getName());
        assertEquals(newSurname, person.getSurName());
        assertEquals(newPerosonType, person.getPersonType());
        assertEquals(newEmail, person.getEmail());
        assertEquals(newAddress, person.getAddress());
        assertEquals(newDate, person.getBirthDate());
    }


}