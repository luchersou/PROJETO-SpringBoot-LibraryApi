package io.project.libraryapi.controller.dto;

import io.project.libraryapi.model.Author;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorDTO(UUID id,
                        String name,
                        LocalDate birthDate,
                        String nationality) {

    public Author mappingToAuthor(){
        Author author = new Author();
        author.setName(this.name);
        author.setBirthDate(this.birthDate);
        author.setNationality(this.nationality);
        return author;
    }
}
