package io.project.libraryapi.controller;

import io.project.libraryapi.controller.dto.BookRegistrationDTO;
import io.project.libraryapi.controller.dto.BookSearchResultDTO;
import io.project.libraryapi.controller.dto.ResponseError;
import io.project.libraryapi.controller.mappers.BookMapper;
import io.project.libraryapi.exceptions.DuplicateRecordException;
import io.project.libraryapi.model.Book;
import io.project.libraryapi.model.BookGenre;
import io.project.libraryapi.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @GetMapping
    public ResponseEntity<List<BookSearchResultDTO>> search(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "title", required = false)
            String title,
            @RequestParam(value = "authorName", required = false)
            String authorName,
            @RequestParam(value = "genre", required = false)
            BookGenre genre,
            @RequestParam(value = "yearRelease", required = false)
            Integer yearRelease

    ){
        var result = service.search(isbn, title, authorName, genre, yearRelease);
        var list = result
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable String id,
                                       @RequestBody @Valid BookRegistrationDTO dto){
        return service.findById(UUID.fromString(id))
            .map(book -> {
                Book auxiliaryEntity = mapper.toEntity(dto);

                book.setDateRelease(auxiliaryEntity.getDateRelease());
                book.setIsbn(auxiliaryEntity.getIsbn());
                book.setPrice(auxiliaryEntity.getPrice());
                book.setGenre(auxiliaryEntity.getGenre());
                book.setTitle(auxiliaryEntity.getTitle());
                book.setAuthor(auxiliaryEntity.getAuthor());

                service.update(book);

                return ResponseEntity.noContent().build();
            }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
