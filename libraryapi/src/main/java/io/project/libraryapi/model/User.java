package io.project.libraryapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "tb_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String login;

    private String password;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private UserRole userRole;
}
