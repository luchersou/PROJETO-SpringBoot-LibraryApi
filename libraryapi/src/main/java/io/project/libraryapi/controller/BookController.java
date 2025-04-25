package io.project.libraryapi.controller;

import io.project.libraryapi.controller.dto.BookRegistrationDTO;
import io.project.libraryapi.controller.dto.BookSearchResultDTO;
import io.project.libraryapi.controller.dto.ResponseError;
import io.project.libraryapi.exceptions.DuplicateRecordException;
import io.project.libraryapi.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid BookRegistrationDTO dto){
        try {

            return ResponseEntity.ok(dto);
        } catch (DuplicateRecordException e){
            var errorDTO = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }
    }

}
