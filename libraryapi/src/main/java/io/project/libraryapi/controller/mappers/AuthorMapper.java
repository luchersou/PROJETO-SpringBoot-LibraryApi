package io.project.libraryapi.controller.mappers;

import io.project.libraryapi.controller.dto.AuthorDTO;
import io.project.libraryapi.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    // Use "@Mapping(source = "name_example", target = "name")" when the source and target names differ between the entity and DTO
    Author toEntity(AuthorDTO dto);

    AuthorDTO toDTO(Author author);
}
