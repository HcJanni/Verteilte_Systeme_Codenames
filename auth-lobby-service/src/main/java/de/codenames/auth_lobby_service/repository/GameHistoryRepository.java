package de.codenames.auth_lobby_service.repository;

import de.codenames.auth_lobby_service.model.GameHistory;
import de.codenames.auth_lobby_service.model.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {
    List<GameHistory> findByLobby(Lobby lobby);
}
