package io.project.libraryapi.controller.common;

import io.project.libraryapi.model.User;
import io.project.libraryapi.model.UserRole;
import io.project.libraryapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagerUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        var userManager = userRepository.findByLogin("manager");

        if (userManager == null) {
            var user = new User();
            user.setLogin("manager");
            user.setPassword(passwordEncoder.encode("123"));
            user.setUserRole(UserRole.MANAGER);
            userRepository.save(user);

            System.out.println("MANAGER user successfully created.");
        } else {
            System.out.println("MANAGER user already exists.");
        }
    }
}
