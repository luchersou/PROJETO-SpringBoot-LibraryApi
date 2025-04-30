package io.project.libraryapi.controller.validator;

import io.project.libraryapi.exceptions.DuplicateRecordException;
import io.project.libraryapi.exceptions.InvalidFieldException;
import io.project.libraryapi.model.Book;
import io.project.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookValidator {

    private static final int YEAR_OF_MANDATORY_PRICE = 2005;

    private final BookRepository repository;

    public void validate(Book book){
        if(bookWithIsbnExists(book)){
            throw new DuplicateRecordException("ISBN already exists in the database.");
        }

        if(isPriceRequiredNull(book)){
            throw new InvalidFieldException("price", "The price field is required for books published from 2020 onward.");
        }
    }

    private boolean isPriceRequiredNull(Book book) {
        return book.getPrice() == null && book.getDateRelease().getYear() >= YEAR_OF_MANDATORY_PRICE;
    }

    private boolean bookWithIsbnExists(Book book){
        Optional<Book> bookFound = repository.findByIsbn(book.getIsbn());

        if(book.getId() == null){
            return bookFound.isPresent();
        }

        return bookFound
                .map(Book::getId)
                .stream()
                .anyMatch(id -> id.equals(book.getId()));
    }
}
