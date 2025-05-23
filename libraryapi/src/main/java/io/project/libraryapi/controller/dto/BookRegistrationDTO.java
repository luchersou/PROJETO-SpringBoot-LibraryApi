package io.project.libraryapi.controller.dto;

import io.project.libraryapi.model.BookGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BookRegistrationDTO(@ISBN(message = "ISBN invalid!")
                                  @NotBlank(message = "Field required!")
                                  String isbn,
                                  @NotNull(message = "Field required!")
                                  String title,
                                  @Past(message = "It can't be a future date!")
                                  @NotNull(message = "Field required!")
                                  LocalDate dateRelease,
                                  BookGenre genre,
                                  BigDecimal price,
                                  @NotNull(message = "Field required!")
                                  UUID idAuthor) {
}
