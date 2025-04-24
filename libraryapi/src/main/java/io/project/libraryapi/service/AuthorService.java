package io.project.libraryapi.service;

import io.project.libraryapi.controller.validator.AuthorValidator;
import io.project.libraryapi.exceptions.NotAllowedOperationException;
import io.project.libraryapi.model.Author;
import io.project.libraryapi.repository.AuthorRepository;
import io.project.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository repository;
    private final AuthorValidator validator;
    private final BookRepository bookRepository;

    public Author save(Author author){
        validator.validate(author);
        return repository.save(author);
    }

    public void update(Author author){
        if(author.getId() == null){
            throw new IllegalArgumentException("It's necessary that the author already be saved in the database");
        }
        validator.validate(author);
        repository.save(author);
    }

    public Optional<Author> findById(UUID id){
        return repository.findById(id);
    }

    public void delete(Author author){
        if(hasBook(author)){
            throw new NotAllowedOperationException("It is not allowed to delete an author with registered books!");
        }
        repository.delete(author);
    }

    public List<Author> search(String name, String nationality){
        if(name != null && nationality != null ){
            return repository.findBynameAndNationality(name, nationality);
        }

        if(name != null){
            return repository.findByName(name);
        }

        if(nationality != null){
            return repository.findByNationality(nationality);
        }

        return repository.findAll();
    }

    public boolean hasBook(Author author){
        return bookRepository.existsByAuthor(author);
    }
}
