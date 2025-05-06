package io.project.libraryapi.security;

import io.project.libraryapi.model.User;
import io.project.libraryapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService service;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getName();
        String typedPassword = authentication.getCredentials().toString();

        User userFound = service.findByLogin(login);

        if(userFound == null){
            throw getUsernameNotFoundException();
        }

        String passwordEnconded = userFound.getPassword();

        boolean passwordMatch = encoder.matches(typedPassword, passwordEnconded);

        if(passwordMatch){
            return new CustomAuthentication(userFound);
        }

        throw getUsernameNotFoundException();

    }

    private static UsernameNotFoundException getUsernameNotFoundException() {
        return new UsernameNotFoundException("User and/or password not found.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
