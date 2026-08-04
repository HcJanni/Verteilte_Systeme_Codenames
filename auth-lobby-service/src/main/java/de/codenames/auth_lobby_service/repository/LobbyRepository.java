package de.codenames.auth_lobby_service.repository;

import de.codenames.auth_lobby_service.model.Lobby;
import de.codenames.auth_lobby_service.model.LobbyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Datenbankzugriff auf {@link Lobby}.
 */
public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    /**
     * Sucht eine Lobby anhand ihres (eindeutigen) Namens.
     *
     * @param lobbyname der gesuchte Lobby-Name
     * @return die gefundene Lobby, oder ein leeres Optional, falls keine existiert
     */
    Optional<Lobby> findByName(String lobbyname);

    /**
     * Liefert alle Lobbys mit dem angegebenen Status.
     *
     * Quellen:
     * https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
     *
     * @param status der gesuchte Status (z.B. {@link LobbyStatus#OPEN})
     * @return Liste aller passenden Lobbys, ggf. leer
     */
    List<Lobby> findByStatus(LobbyStatus status);
}
