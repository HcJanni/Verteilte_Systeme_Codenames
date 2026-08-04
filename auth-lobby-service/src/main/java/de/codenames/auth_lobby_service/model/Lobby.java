package de.codenames.auth_lobby_service.model;

import jakarta.persistence.*;

/**
 * Repräsentiert eine Spiel-Lobby ("Warteraum") vor bzw. während einer Partie.
 * <p>
 * Enthält nur die Verwaltungsdaten der Lobby selbst; der eigentliche Spielzustand
 * (Board, Karten) wird separat im Game-Service verwaltet, siehe {@code GameState}.
 */
@Entity
public class Lobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    /** Aktueller Lebenszyklus-Status, siehe {@link LobbyStatus}. */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LobbyStatus status;

    /** Gewählter Spielmodus, siehe {@link GameMode}. */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameMode gameMode;

    /** Der Nutzer, der diese Lobby erstellt hat. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private User createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LobbyStatus getStatus() {
        return status;
    }

    public void setStatus(LobbyStatus status) {
        this.status = status;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
