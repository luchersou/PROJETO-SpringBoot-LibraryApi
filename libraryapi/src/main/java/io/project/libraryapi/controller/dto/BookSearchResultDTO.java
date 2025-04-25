package io.project.libraryapi.controller.dto;

import io.project.libraryapi.model.BookGenre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BookSearchResultDTO(UUID id,
                                  String isbn,
                                  String title,
                                  LocalDate dateRelease,
                                  BookGenre genre,
                                  BigDecimal price,
                                  AuthorDTO author) {
}
