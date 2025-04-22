package io.project.libraryapi.service;

import io.project.libraryapi.model.Author;
import io.project.libraryapi.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public Author save(Author author){
        return repository.save(author);
    }

    public Optional<Author> findById(UUID id){
        return repository.findById(id);
    }
}
