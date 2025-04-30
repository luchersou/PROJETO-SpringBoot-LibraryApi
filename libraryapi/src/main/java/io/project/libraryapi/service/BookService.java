package io.project.libraryapi.service;

import io.project.libraryapi.controller.validator.BookValidator;
import io.project.libraryapi.model.Book;
import io.project.libraryapi.model.BookGenre;
import io.project.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

// This is necessary for the specifications. Without it, you'd need to put this way: specs = specs.and(BookSpecs.isbnEqual(isbn));
import static io.project.libraryapi.repository.Specs.BookSpecs.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final BookValidator validator;

    public Book save(Book book) {
        validator.validate(book);
        return repository.save(book);
    }

    public Optional<Book> findById(UUID id) {
        return repository.findById(id);
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    public Page<Book> search(String isbn,
                             String title,
                             String authorName,
                             BookGenre genre,
                             Integer yearRelease,
                             Integer page,
                             Integer pageLength) {


        // It's basically an 'always true,' a starting point to keep adding filters.
        Specification<Book> specs = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction() );

        //these are the filters:
        if(isbn != null){
            // query = query and isbn = :isbn
            specs = specs.and(isbnEqual(isbn));
        }

        if(title != null){
            // query = query AND title LIKE %:title%
            specs = specs.and(titleLike(title));
        }

        if(genre != null){
            // query = query AND genre = :genre
            specs = specs.and(genreEqual(genre));
        }

        if(yearRelease != null){
            specs = specs.and(yearReleaseEqual(yearRelease));
        }

        if(authorName != null){
            specs = specs.and(nameAuthorLike(authorName));
        }

        Pageable pageRequest = PageRequest.of(page, pageLength);

        return repository.findAll(specs, pageRequest);
    }

    public void update(Book book) {
        if(book.getId() == null){
            throw new IllegalArgumentException("It's necessary that the book already be saved in the database");
        }
        validator.validate(book);
        repository.save(book);
    }
}
