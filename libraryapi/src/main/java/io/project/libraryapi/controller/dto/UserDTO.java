package io.project.libraryapi.controller.dto;

import io.project.libraryapi.model.UserRole;

public record UserDTO(String login, String password, UserRole userRole){
}
