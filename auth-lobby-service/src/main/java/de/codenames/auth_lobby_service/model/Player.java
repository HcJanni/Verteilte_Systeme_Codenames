package de.codenames.auth_lobby_service.model;

import jakarta.persistence.*;

/**
 * Verknüpft einen {@link User} mit einer {@link Lobby}, bildet die Mitgliedschaft
 * eines Spielers in einer Partie ab.
 */
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Der Nutzer, der dieser Lobby beigetreten ist. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    /** Die Lobby, der beigetreten wurde. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "lobby_id", nullable = false, updatable = false)
    private Lobby lobby;

    //@Column(nullable = false)
    //private int Team;

    /** Rolle im Classic-Modus, siehe {@link PlayerRole}. Bleibt {@code null} im Duett-Modus. */
    @Column
    @Enumerated(EnumType.STRING)
    private PlayerRole playerRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }
}
