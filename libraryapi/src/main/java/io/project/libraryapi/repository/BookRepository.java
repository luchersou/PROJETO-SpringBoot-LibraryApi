package io.project.libraryapi.repository;

import io.project.libraryapi.model.Author;
import io.project.libraryapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    boolean existsByAuthor(Author author);
}
