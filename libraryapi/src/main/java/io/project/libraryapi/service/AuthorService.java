package io.project.libraryapi.service;

import io.project.libraryapi.model.Author;
import io.project.libraryapi.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void delete(Author author){
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
}
