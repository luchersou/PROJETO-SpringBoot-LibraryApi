package io.project.libraryapi.controller;

import io.project.libraryapi.controller.dto.UserDTO;
import io.project.libraryapi.controller.mappers.UserMapper;
import io.project.libraryapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid UserDTO dto){
        var user = mapper.toEntity(dto);
        service.save(user);
    }
}
