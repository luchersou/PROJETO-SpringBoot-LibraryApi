package io.project.libraryapi.controller;

import io.project.libraryapi.controller.dto.AuthorDTO;
import io.project.libraryapi.controller.dto.ResponseError;
import io.project.libraryapi.exceptions.DuplicateRecordException;
import io.project.libraryapi.exceptions.NotAllowedOperationException;
import io.project.libraryapi.model.Author;
import io.project.libraryapi.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService service;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid AuthorDTO author) {
        try{
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
        } catch (DuplicateRecordException e){
            var errorDTO = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }
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

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable String id){
        try {
            var idAuthor = UUID.fromString(id);
            Optional<Author> authorOptional = service.findById(idAuthor);

            if (authorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            service.delete(authorOptional.get());
            return ResponseEntity.noContent().build();
        } catch (NotAllowedOperationException e){
            var responseError = ResponseError.defaultResponse(e.getMessage());
            return ResponseEntity.status(responseError.status()).body(responseError);
        }
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> search(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "nationality", required = false) String nationality){
        List<Author> result = service.search(name, nationality);
        List<AuthorDTO> list = result
                .stream()
                .map(author -> new AuthorDTO(
                        author.getId(),
                        author.getName(),
                        author.getBirthDate(),
                        author.getNationality())
                ).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> Update(@PathVariable String id,
                                       @RequestBody @Valid AuthorDTO dto) {

        try {
            var idAuthor = UUID.fromString(id);
            Optional<Author> authorOptional = service.findById(idAuthor);

            if (authorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var author = authorOptional.get();
            author.setName(dto.name());
            author.setBirthDate(dto.birthDate());
            author.setNationality(dto.nationality());

            service.save(author);

            return ResponseEntity.noContent().build();
        } catch (DuplicateRecordException e){
            var errorDTO = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }
    }
}
