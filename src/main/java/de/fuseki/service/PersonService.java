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

    public void deletePerson(Integer id) {
        try {
            personRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new IdNotFoundException("Id can´t be found.");
        }
    }

    public PersonDto addNewPerson(PersonDto personDto) {
        if (personDto.getId() != null) {
            throw new IdShouldBeNullException("The Id has to be 0 or Null, because the id is given by the Server.");
        }
        if (personRepository.existsByEmail(personDto.getEmail())){
            throw new EmailAlreadyExistsException();
        }
        Person mappedPerson = personMapper.toEntity(personDto);
        Person returnedPerson = personRepository.save(mappedPerson);
        return personMapper.toDto(returnedPerson);
    }

    @Transactional
    public PersonDto updatePerson(PersonDto personDto) {
        Person person;
        if (!personRepository.existsById(personDto.getId())) {
            throw new IdNotFoundException("Id is invalid.");
        }
        person = personRepository.getReferenceById(personDto.getId());

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