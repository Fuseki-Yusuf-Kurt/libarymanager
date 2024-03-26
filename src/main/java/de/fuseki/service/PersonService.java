package de.fuseki.service;

import de.fuseki.converter.PersonDtoConverterUtil;
import de.fuseki.dtos.PersonDto;
import de.fuseki.entities.Address;
import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import de.fuseki.exceptions.IdNotFoundException;
import de.fuseki.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;


    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public void deletePerson(int id) {

        personRepository.deleteById(id);
    }

    public PersonDto addNewPerson(Person person) {
        personRepository.save(person);
        return PersonDtoConverterUtil.convertPersonToPersonDto(person);
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
            throw new RuntimeException();//TODO Exception machen
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
        return PersonDtoConverterUtil.convertPersonToPersonDto(person);
    }
}