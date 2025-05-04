package io.project.libraryapi.security;

import io.project.libraryapi.model.Author;
import io.project.libraryapi.model.User;
import io.project.libraryapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService service;

    public User findLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String login = userDetails.getUsername();
        return service.findByLogin(login);
    }
}
