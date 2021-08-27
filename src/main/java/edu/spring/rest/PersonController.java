package edu.spring.rest;

import edu.spring.repostory.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PersonController {

    private final PersonRepository repository;

    @Autowired
    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/persons")
    List<PersonDto> getPersons() {
        return repository.findAll()
                .stream()
                .map(PersonDto::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/person/{id}")
    PersonDto getPersonById(@PathVariable int id) {
        return repository.findById(id)
                .map(PersonDto::toDto)
                .orElse(null);
    }

    @PostMapping("/person")
    PersonDto postPerson(@RequestBody PersonDto person) {
        if (!repository.existsById(person.getId())) {
            repository.save(PersonDto.toDomainObject(person));
            return person;
        }
        return null;
    }

    @PutMapping("/person")
    PersonDto putPerson(@RequestBody PersonDto person) {
        if (repository.existsById(person.getId())) {
            repository.deleteById(person.getId());
        }
        repository.save(PersonDto.toDomainObject(person));
        return person;
    }

    @DeleteMapping("/person/{id}")
    ResponseEntity<String> deletePersonById(@PathVariable int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("ok");
        }
        return ResponseEntity.notFound().build();
    }
}
