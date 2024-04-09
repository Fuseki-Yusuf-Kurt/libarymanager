package de.fuseki.service;

import de.fuseki.dtos.PersonDto;
import de.fuseki.entities.Address;
import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import de.fuseki.exceptions.EmailAlreadyExistsException;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.exceptions.IdShouldBeNullException;
import de.fuseki.mapper.PersonMapperImpl;
import de.fuseki.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapperImpl personMapper;

    public List<PersonDto> getAllPersons() {
        List<Person> personList = personRepository.findAll();
        List<PersonDto> personDtos = personMapper.toDtoList(personList);
        return personDtos;
    }

    public PersonDto getPerson(Integer id) {
        if (!personRepository.existsById(id)) {
            throw new IdNotFoundException("Id can´t be found.");
        }
        PersonDto foundPerson = personMapper.toDto(personRepository.getReferenceById(id));
        return foundPerson;
    }

    public void deletePerson(Integer id) {
        if (!personRepository.existsById(id)) {
            throw new IdNotFoundException("Id can´t be found.");
        }
        personRepository.deleteById(id);
    }

    public PersonDto addNewPerson(PersonDto personDto) {
        if (personDto.getId() != null) {
            throw new IdShouldBeNullException("The Id has to be 0 or Null, because the id is given by the Server.");
        }
        if (personRepository.existsByEmail(personDto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        Person mappedPerson = personMapper.toEntity(personDto);
        Person returnedPerson = personRepository.save(mappedPerson);
        return personMapper.toDto(returnedPerson);
    }

    @Transactional
    public PersonDto updatePerson(PersonDto personDto) {
        Person person;
        try {
            person = personRepository.getReferenceById(personDto.getId());

        }catch (IllegalArgumentException illegalArgumentException){
            throw new IdNotFoundException("Id should not be null.");
        }catch (EntityNotFoundException entityNotFoundException){
            throw new IdNotFoundException("Id does not exist.");
        }

        String newEmail = personDto.getEmail();
        if (newEmail != null &&
                !newEmail.isEmpty() &&
                !newEmail.equals(person.getEmail()) &&
                personRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException();
        }

        if (newEmail != null && !newEmail.isEmpty()) {
            person.setEmail(newEmail);
        }

        String newName = personDto.getName();
        if (newName != null && !newName.isEmpty()) {
            person.setName(newName);
        }

        String newSurname = personDto.getSurName();
        if (newSurname != null && !newSurname.isEmpty()) {
            person.setSurName(newSurname);
        }

        PersonType newPersonType = personDto.getPersonType();
        if (newPersonType != null && newPersonType != person.getPersonType()) {
            person.setPersonType(newPersonType);
        }

        Address newAddress = personDto.getAddress();
        if (newAddress != null) {
            person.setAddress(newAddress);
        }

        LocalDate newBirthDate = personDto.getBirthDate();
        if (newBirthDate != null) {
            person.setBirthDate(newBirthDate);
        }
        return personMapper.toDto(person);
    }

}