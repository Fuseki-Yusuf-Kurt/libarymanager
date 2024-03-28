package de.fuseki.service;

import de.fuseki.dtos.PersonDto;
import de.fuseki.entities.Address;
import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import de.fuseki.exceptions.IdShouldBeNullException;
import de.fuseki.mapper.PersonMapperImpl;
import de.fuseki.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class PersonServiceTest {
    @Mock
    PersonRepository personRepository;
    @InjectMocks
    private PersonService underTest;
    @Mock
    PersonMapperImpl personMapper;

    @Test
    void gestsAllPersons() {
        //Given
        Person person = new Person(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        PersonDto personDto = new PersonDto(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        //Mocking
        when(personMapper.toDtoList(List.of(person))).thenReturn(List.of(personDto));
        when(personRepository.findAll()).thenReturn(List.of(person));
        //When
        List<PersonDto> personDtoList = underTest.getAllPersons();
        //Then
        assertEquals(personMapper.toDtoList(List.of(person)), personDtoList);
    }

    @Test
    void deletePerson() {
        //Mocking
        when(personRepository.existsById(1)).thenReturn(true);
        //When
        underTest.deletePerson(1);
        //Then
        verify(personRepository, times(1)).deleteById(1);
    }

    @Test
    void addNewPerson() {
        //Given
        PersonDto personDto = new PersonDto(null, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        PersonDto personDtoWithId = new PersonDto(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        Person person = new Person(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        //Mock
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toEntity(personDto)).thenReturn(person);
        when(personMapper.toDto(person)).thenReturn(personDtoWithId);
        //When
        PersonDto returnedPersonDto = underTest.addNewPerson(personDto);
        //Then
        verify(personRepository, times(1)).save(person);
        assertEquals(returnedPersonDto.getPersonType(), person.getPersonType());
        assertEquals(returnedPersonDto.getName(), person.getName());
        assertEquals(returnedPersonDto.getSurName(), person.getSurName());
        assertEquals(returnedPersonDto.getEmail(), person.getEmail());
        assertEquals(returnedPersonDto.getBirthDate(), person.getBirthDate());
        assertNotNull(returnedPersonDto.getId());
    }

    @Test
    void addNewPersonReturnsException() {
        //Given
        PersonDto personDto = new PersonDto(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        PersonDto personDtoWithId = new PersonDto(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        Person person = new Person(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));

        //When

        assertThrowsExactly(IdShouldBeNullException.class,() ->
                underTest.addNewPerson(personDto));
    }

    @Test
    void updatePersonThrowsExceptionBecouseIdDoesNotExist() {
        //Mocking
        when(personRepository.existsById(1)).thenReturn(false);
        //Then
        assertThrows(RuntimeException.class, () -> {
            underTest.updatePerson(new PersonDto(1, null, null, null, null, null, null));
        });
    }

    @Test
    void updatePersonThrowsExceptionBecouseEmailAlreadyExists() {
        //Given
        Person person = new Person(1, "testname", "testsurname", PersonType.CLIENT, "testemail@test.test", new Address("test", "test", "test", "test"), LocalDate.parse("2000-01-01"));
        //Mocking
        when(personRepository.existsById(1)).thenReturn(true);
        when(personRepository.getReferenceById(1)).thenReturn(person);
        when(personRepository.existsByEmail("existingmail@test.test")).thenReturn(true);
        //Then
        assertThrows(RuntimeException.class, () -> {
            underTest.updatePerson(new PersonDto(1,
                    null,
                    null,
                    null,
                    "existingmail@test.test",
                    null,
                    null));
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
        Address newAddress = new Address("newStreet", "newNumber", "newCity", "newPostalCode");
        //Mocking
        when(personRepository.existsById(1)).thenReturn(true);
        when(personRepository.getReferenceById(1)).thenReturn(person);
        when(personRepository.existsByEmail(newEmail)).thenReturn(false);

        //When
        underTest.updatePerson(new PersonDto(1, newName, newSurname, newPerosonType, newEmail, newAddress, newDate));

        //Then
        assertEquals(newName, person.getName());
        assertEquals(newSurname, person.getSurName());
        assertEquals(newPerosonType, person.getPersonType());
        assertEquals(newEmail, person.getEmail());
        assertEquals(newAddress, person.getAddress());
        assertEquals(newDate, person.getBirthDate());
    }


}