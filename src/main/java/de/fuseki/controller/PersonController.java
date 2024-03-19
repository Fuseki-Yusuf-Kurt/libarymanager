package de.fuseki.controller;

import de.fuseki.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("person")
public class PersonController {
    @Autowired
    PersonService personService;

    public PersonController(PersonService personService){
        this.personService = personService;
    }

    @GetMapping
    public List<Person> getAllPersons(){
        return personService.getAllPersons;
    }

    @DeleteMapping("{studentId}")
    public void deletePerson(@PathVariable("studentId")int id){
        personService.deletePerson(id);
    }

    @PostMapping
    public void addPerson(@RequestBody Person person){
        personService.addNewPerson(person);
    }

    @PutMapping("{studentId}")
    public void updateStudent(
            @PathVariable("studentId") int id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String personType,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String birthDate
    ){
        personService.updateStudent(name,surname,personType,email,address,birthDate);
    }

}
