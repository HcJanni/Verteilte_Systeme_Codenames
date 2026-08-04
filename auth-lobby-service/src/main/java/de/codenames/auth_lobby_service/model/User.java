package de.codenames.auth_lobby_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

// https://spring.io/guides/gs/accessing-data-jpa

/**
 * Repräsentiert einen registrierten Nutzer-Account.
 * <p>
 * Wird sowohl für Registrierung/Login (siehe {@link de.codenames.auth_lobby_service.controller.AuthController}) als auch
 * als Referenz für erstellte Lobbys und Spieler-Mitgliedschaften verwendet.
 */
@Entity
@Table(name="users",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "username"),
            @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Eindeutiger Anmeldename, dient als Login-Kennung. */
    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    /** BCrypt-Hash des Passworts, das Passwort wird nicht als Klartext speichern. */
    @NotBlank
    @Size(max = 120)
    private String password;

    // https://balasubramanyamlanka.com/understanding-the-difference-between-instant.now().toepochmilli()-and-system.currenttimemillis()-in-java/
    private Instant createdAt = Instant.now();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /** Zeitpunkt der Registrierung (UTC). */
    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
