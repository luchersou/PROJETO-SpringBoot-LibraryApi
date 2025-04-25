package io.project.libraryapi.service;

import io.project.libraryapi.model.Book;
import io.project.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

}
