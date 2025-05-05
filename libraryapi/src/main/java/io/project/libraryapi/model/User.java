package io.project.libraryapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
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

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private UserRole userRole;
}
