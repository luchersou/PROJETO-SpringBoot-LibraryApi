package io.project.libraryapi.controller.dto;

import io.project.libraryapi.model.Author;

import java.time.LocalDate;

public record AuthorDTO(String name,
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
