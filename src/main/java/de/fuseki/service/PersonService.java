package de.fuseki.service;

import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
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
    public void updatePerson(Integer id, String name, String surname, PersonType personType, String email, String address, LocalDate birthDate) {
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
        if (personType != null && personType != person.getPersonType()){
            person.setPersonType(personType);
        }
        if (email != null && !email.isEmpty() && !email.equals(person.getEmail())){
            if (!personRepository.existsByEmail(email)) {
                person.setEmail(email);
            }else throw new RuntimeException();//TODO Exception machen
        }
        if (address != null && !address.isEmpty() && !address.equals(person.getAddress())){
            person.setAddress(address);
        }
        if (birthDate != null && !birthDate.equals(person.getBirthDate())){
            person.setBirthDate(birthDate);
        }
    }
}