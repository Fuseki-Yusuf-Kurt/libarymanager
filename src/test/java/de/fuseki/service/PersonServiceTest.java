package de.fuseki.service;

import de.fuseki.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    PersonRepository personRepository;
    private AutoCloseable autoCloseable;
    private PersonService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PersonService(personRepository);
    }

    @Test
    void gestsAllPersons() {
        //When
        underTest.getAllPersons();
        //Then
        verify(personRepository).findAll();
    }

    @Test
    @Disabled
    void deletePerson() {
    }

    @Test
    @Disabled
    void addNewPerson() {
    }

    @Test
    @Disabled
    void updatePerson() {
    }
}