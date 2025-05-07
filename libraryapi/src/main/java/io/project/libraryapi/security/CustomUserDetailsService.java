package io.project.libraryapi.security;

import io.project.libraryapi.model.User;
import io.project.libraryapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = service.findByLogin(login);

        if (user == null) {
            user = service.findByEmail(login);
        }

        if (user == null){
            throw new UsernameNotFoundException("User not found!");
        }
        // I need to use the URL path because my entity and the class name are the same
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getUserRole().toString())
                .build();
    }
}
