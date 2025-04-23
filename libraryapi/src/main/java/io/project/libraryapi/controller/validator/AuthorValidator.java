package io.project.libraryapi.controller.validator;

import io.project.libraryapi.exceptions.DuplicateRecordException;
import io.project.libraryapi.model.Author;
import io.project.libraryapi.repository.AuthorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorValidator {

    private AuthorRepository repository;

    public AuthorValidator(AuthorRepository repository) {
        this.repository = repository;
    }

    public void validate(Author author){
        if(authorRegistered(author)){
            throw new DuplicateRecordException("author already registered!");
        }
    }

    private boolean authorRegistered(Author author){
        Optional<Author> authorFound = repository.findByNameAndBirthDateAndNationality(
                author.getName(), author.getBirthDate(), author.getNationality()
        );

        if (author.getId() == null){
            return authorFound.isPresent();
        }

        return !author.getId().equals(authorFound.get().getId()) && authorFound.isPresent();
    }
}
