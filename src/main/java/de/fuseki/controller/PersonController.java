package de.fuseki.controller;

import de.fuseki.dtos.PersonDto;
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

    @GetMapping("/user/{id}")
    public PersonDto getPerson(@PathVariable("id") Integer id){
        return personService.getPerson(id);
    }
    @DeleteMapping("/user/{id}")
    public void deletePerson(@PathVariable("id") int id) {
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
