package io.project.libraryapi.controller.mappers;

import io.project.libraryapi.controller.dto.BookRegistrationDTO;
import io.project.libraryapi.controller.dto.BookSearchResultDTO;
import io.project.libraryapi.model.Book;
import io.project.libraryapi.repository.AuthorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

// Use 'uses' because inside BookSearchResultDTO it requires 'AuthorDTO author', which is not available in the 'Book' entity.
@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public abstract class BookMapper {

    @Autowired
    AuthorRepository authorRepository;

    @Mapping(target = "author", expression = "java( authorRepository.findById(dto.idAuthor()).orElse(null) )")
    public abstract Book toEntity(BookRegistrationDTO dto);

    public abstract BookSearchResultDTO toDTO(Book book);

    // The BookMapper requires AuthorRepository injection because it needs to fetch the full Author entity from the database using only an idAuthor provided in the BookRegistrationDTO, whereas the AuthorMapper performs a direct field mapping between AuthorDTO and Author without needing external dependencies.
}
