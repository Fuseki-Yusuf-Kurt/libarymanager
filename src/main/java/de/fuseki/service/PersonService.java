package de.fuseki.service;

import de.fuseki.dtos.PersonDto;
import de.fuseki.dtos.ReservationDto;
import de.fuseki.entities.Person;
import de.fuseki.exceptions.EmailAlreadyExistsException;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.exceptions.IdShouldBeNullException;
import de.fuseki.exceptions.IsNullException;
import de.fuseki.mapper.PersonMapper;
import de.fuseki.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public List<PersonDto> getAllPersons() {
        List<Person> personList = personRepository.findAll();
        List<PersonDto> personDtos = PersonMapper.MAPPER.toDtoList(personList);
        return personDtos;
    }

    @Transactional
    public PersonDto getPerson(Integer id) {
        return PersonMapper.MAPPER.toDto(getPersonFromDatabase(id));
    }

    public void deletePerson(Integer id) {
        if (!personRepository.existsById(id)) {
            throw new IdNotFoundException("Id can´t be found.");
        }
        personRepository.deleteById(id);
    }

    public PersonDto addNewPerson(PersonDto personDto) {
        if (personDto.getId() != null) {
            throw new IdShouldBeNullException("The Id has to be 0 or Null, because the id is given by the server.");
        }
        if (personRepository.existsByEmail(personDto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        if (personDto.getName() == null
                || personDto.getSurName() == null
                || personDto.getEmail() == null
                || personDto.getPersonType() == null
                || personDto.getAddress() == null
                || personDto.getBirthDate() == null
        ) {
            throw new IsNullException("Name, Surname, Email, PersonType, Address and BirthDate can't be null!");
        }
        Person mappedPerson = PersonMapper.MAPPER.toEntity(personDto);
        Person returnedPerson = personRepository.save(mappedPerson);
        return PersonMapper.MAPPER.toDto(returnedPerson);
    }

    public Person getPersonFromDatabase(int id) {
        try {
            Optional<Person> foundPerson = personRepository.findById(id);
            if (foundPerson.isEmpty()) {
                throw new IdNotFoundException("Id not Found!");
            }
            return foundPerson.get();
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IdNotFoundException("Id should not be null.");
        }
    }

    @Transactional
    public PersonDto updatePerson(PersonDto personDto) {
        Person personFromDatabase = getPersonFromDatabase(personDto.getId());

        String newEmail = personDto.getEmail();
        if (newEmail != null &&
                !newEmail.isEmpty() &&
                !newEmail.equals(personFromDatabase.getEmail()) &&
                personRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException();
        }

        PersonMapper.MAPPER.partialUpdate(personDto, personFromDatabase);
        return PersonMapper.MAPPER.toDto(personFromDatabase);
            }

}