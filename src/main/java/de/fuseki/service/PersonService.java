package de.fuseki.service;

import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import de.fuseki.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;


    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public void deletePerson(int id){
        personRepository.deleteById(id);
    }

    public void addNewPerson(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void updatePerson(int id, String name, String surname, String personType, String email, Address address, LocalDate birthDate) {
       Person person;
        if(!personRepository.existsById(id)){
            throw new RuntimeException(); //TODO eigene exception entwickeln.
        }else {
            person = personRepository.getReferenceById(id);
        }
        if (name != null && !name.isEmpty() && !name.equals(person.getName())){
            person.setName(name);
        }
        if (surname != null && !surname.isEmpty() && !surname.equals(person.getSurname())){
            person.setSurname(name);
        }
        if (personType != null && !personType.isEmpty() && PersonType.valueOf(personType) != person.getPersonType()){
            person.setPersonType(PersonType.valueOf(personType));
        }
        if (email != null && !email.isEmpty() && !email.equals(person.getEmail())){
            if (!personRepository.existsByEmail(email)) {
                person.setEmail(email);
            }else throw new RuntimeException();//TODO Exception machen
        }
        if (address != null && !address.equals(person.getAddress())){
            person.setAddress(address);
        }
        if (birthDate != null && !birthDate.equals(person.getBirthDate())){
            person.setBirthDate(birthDate);
        }
    }
}