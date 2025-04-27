package io.project.libraryapi.controller;

import io.project.libraryapi.controller.dto.BookRegistrationDTO;
import io.project.libraryapi.controller.dto.BookSearchResultDTO;
import io.project.libraryapi.controller.dto.ResponseError;
import io.project.libraryapi.controller.mappers.BookMapper;
import io.project.libraryapi.exceptions.DuplicateRecordException;
import io.project.libraryapi.model.Book;
import io.project.libraryapi.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController implements GenericController{

    private final BookService service;
    private final BookMapper mapper;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid BookRegistrationDTO dto) {

        Book book = mapper.toEntity(dto);
        service.save(book);
        var url = generateHeaderLocation(book.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<BookSearchResultDTO> findDetails(@PathVariable String id){
        return service.findById(UUID.fromString(id))
                .map(book -> {
                    var dto = mapper.toDTO(book);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable String id){
        return service.findById(UUID.fromString(id))
                .map(book -> {
                    service.delete(book);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
