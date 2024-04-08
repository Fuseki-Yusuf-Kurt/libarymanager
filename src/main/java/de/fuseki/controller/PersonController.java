package de.fuseki.controller;

import de.fuseki.dtos.PersonDto;
import de.fuseki.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/users")
    public ResponseEntity<List<PersonDto>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(personService.getPerson(id));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deletePerson(@PathVariable("id") int id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user")
    public ResponseEntity<PersonDto> addPerson(@RequestBody PersonDto personDto) {
        return ResponseEntity.ok(personService.addNewPerson(personDto));
    }


    @PutMapping(value = "/user", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PersonDto> updatePerson(
            @RequestBody PersonDto personDto
    ) {
        return ResponseEntity.ok().body(personService.updatePerson(personDto));
    }

}
