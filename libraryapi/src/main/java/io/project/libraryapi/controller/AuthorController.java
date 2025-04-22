package io.project.libraryapi.controller;

import io.project.libraryapi.controller.dto.AuthorDTO;
import io.project.libraryapi.model.Author;
import io.project.libraryapi.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("authors")
public class AuthorController {

    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody AuthorDTO author) {
        Author authorEntity = author.mappingToAuthor();
        service.save(authorEntity);

        // Returns the 'Location' header with the URI of the newly created resource (REST best practice).
        // Clients can immediately access it via GET or use the ID for future operations (PUT/DELETE), avoiding extra database queries.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(authorEntity.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> findDetails(@PathVariable String id){ // Do not need to put "@PathVariable("id")" because the string and the GetMepping have the same name
        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.findById(idAuthor);
        if (authorOptional.isPresent()){
            Author author = authorOptional.get();
            AuthorDTO dto = new AuthorDTO(author.getId(),
                    author.getName(),
                    author.getBirthDate(),
                    author.getNationality());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

}
