package de.fuseki.controller;

import de.fuseki.entities.Person;
import de.fuseki.enums.PersonType;
import de.fuseki.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
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
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) PersonType personType,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Address address,
            @RequestParam(required = false) LocalDate birthDate
    ){
        personService.updatePerson(id,name,surname,personType,email,address,birthDate);
    }

}
