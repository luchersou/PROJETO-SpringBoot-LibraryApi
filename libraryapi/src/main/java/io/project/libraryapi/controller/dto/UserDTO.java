package io.project.libraryapi.controller.dto;

import io.project.libraryapi.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDTO(@NotBlank(message = "Field required!")
                      String login,
                      @Email(message = "Invalid email")
                      String email,
                      @NotBlank(message = "Field required!")
                      String password,
                      UserRole userRole){
}
