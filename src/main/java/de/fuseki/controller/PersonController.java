package de.fuseki.controller;

import de.fuseki.entities.Address;
import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import de.fuseki.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping//TODO auf users wechseln
    public List<Person> getAllPersons(){
        return personService.getAllPersons();
    }

    @DeleteMapping("{studentId}")
    public void deletePerson(@PathVariable("studentId")int id){
        personService.deletePerson(id);
    }

    @PostMapping
    public void addPerson(@RequestBody Person person){
        personService.addNewPerson(person);
    }

    @PutMapping("{personId}")
    public void updatePerson(
            @PathVariable("personId") Integer id,
            @RequestBody(required = false) String name,
            @RequestBody(required = false) String surname,
            @RequestBody(required = false) PersonType personType,
            @RequestBody(required = false) String email,
            @RequestBody(required = false) Address address,
            @RequestBody(required = false) LocalDate birthDate
    ){
        personService.updatePerson(id,name,surname,personType,email,address,birthDate);
    }

}
