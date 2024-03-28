package de.fuseki.controller;

import de.fuseki.dtos.PersonDto;
import de.fuseki.entities.Person;
import de.fuseki.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/users")
    public List<PersonDto> getAllPersons() {
        return personService.getAllPersons();
    }

    @DeleteMapping("/user/{userId}")
    public void deletePerson(@PathVariable("userId") int id) {
        personService.deletePerson(id);
    }

    @PostMapping("/user")
    public PersonDto addPerson(@RequestBody PersonDto personDto) {
        return personService.addNewPerson(personDto);
    }


    @PutMapping(value = "/user", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public PersonDto updatePerson(
            @RequestBody PersonDto personDto
    ) {
        return personService.updatePerson(personDto);
    }

}
