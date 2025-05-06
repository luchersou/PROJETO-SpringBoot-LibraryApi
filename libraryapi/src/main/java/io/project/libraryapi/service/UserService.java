package io.project.libraryapi.service;

import io.project.libraryapi.model.User;
import io.project.libraryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public void save(User user){
        var password = user.getPassword();
        user.setPassword(encoder.encode(password));
        repository.save(user);
    }

    public User findByLogin(String login){
        return repository.findByLogin(login);
    }

    public User findByemail(String email){
        return repository.findByEmail(email);
    }
}
