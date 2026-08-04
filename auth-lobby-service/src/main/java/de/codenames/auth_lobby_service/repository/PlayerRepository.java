package de.codenames.auth_lobby_service.repository;

import de.codenames.auth_lobby_service.model.Lobby;
import de.codenames.auth_lobby_service.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Datenbankzugriff auf {@link Player}.
 */
public interface PlayerRepository extends JpaRepository<Player, Long> {
    /**
     * Liefert alle Spieler(-Mitgliedschaften) einer Lobby.
     *
     * @param lobby die Lobby, deren Mitglieder gesucht werden
     * @return Liste aller Spieler in dieser Lobby, ggf. leer
     */
    List<Player> findByLobby(Lobby lobby);
}
