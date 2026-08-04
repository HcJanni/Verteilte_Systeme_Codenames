package de.codenames.auth_lobby_service.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class GameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lobby_id", nullable = false, updatable = false)
    private Lobby lobby;

    @Column
    @Enumerated(EnumType.STRING)
    private GameOutcome gameOutcome;

    private Instant endedAt = Instant.now();

    private int turnCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public GameOutcome getGameOutcome() {
        return gameOutcome;
    }

    public void setGameOutcome(GameOutcome gameOutcome) {
        this.gameOutcome = gameOutcome;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }
}
