package io.project.libraryapi.controller;

import io.project.libraryapi.controller.dto.AuthorDTO;
import io.project.libraryapi.controller.dto.ResponseError;
import io.project.libraryapi.controller.mappers.AuthorMapper;
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
public class AuthorController implements GenericController{

    private final AuthorService service;
    private final AuthorMapper mapper;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid AuthorDTO dto) {

        Author author = mapper.toEntity(dto);
        service.save(author);
        URI location = generateHeaderLocation(author.getId());
        return ResponseEntity.created(location).build();

    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> findDetails(@PathVariable String id) { // Do not need to put "@PathVariable("id")" because the string and the GetMepping have the same name
        var idAuthor = UUID.fromString(id);

        return service.findById(idAuthor)
                .map(author -> {
                    AuthorDTO dto = mapper.toDTO(author);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOptional = service.findById(idAuthor);

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.delete(authorOptional.get());
        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> search(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "nationality", required = false) String nationality) {
        List<Author> result = service.search(name, nationality);
        List<AuthorDTO> list = result
                .stream()
                .map(mapper::toDTO) // It's the same as "author -> mapper.toDTO(author)"
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> Update(@PathVariable String id,
                                         @RequestBody @Valid AuthorDTO dto) {


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
    }
}
