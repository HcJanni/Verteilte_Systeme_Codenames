package de.codenames.auth_lobby_service.repository;

import de.codenames.auth_lobby_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// https://spring.io/guides/gs/accessing-data-jpa
// Wieso das? Woher?
/**
 * Datenbankzugriff auf {@link User}.
 * <p>
 * Die Methoden werden von Spring Data JPA automatisch implementiert
 * (Query-Ableitung aus dem Methodennamen).
 */
public interface UserRepository extends JpaRepository<User, Long> {
    // Was das?
    /**
     * Sucht einen Nutzer anhand seines Anmeldenamens.
     *
     * @param username der gesuchte Anmeldename
     * @return der gefundene Nutzer, oder ein leeres Optional, falls keiner existiert
     */
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
