package io.project.libraryapi.service;

import io.project.libraryapi.model.Book;
import io.project.libraryapi.model.BookGenre;
import io.project.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

    public Book save(Book book) {
        return repository.save(book);
    }

    public Optional<Book> findById(UUID id){
        return repository.findById(id);
    }

    public void delete(Book book){
        repository.delete(book);
    }

    public List<Book> search(String isbn, String authorName, BookGenre genre, Integer yearRelease){
        Specification<Book> specification = null;
        return repository.findAll(specification);
    }
}
