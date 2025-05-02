package io.project.libraryapi.controller.mappers;

import io.project.libraryapi.controller.dto.UserDTO;
import io.project.libraryapi.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO dto);

}
