package de.codenames.auth_lobby_service.model;

/**
 * Spielmodus einer {@link Lobby}.
 */
public enum GameMode {
    /** Klassischer Modus mit zwei Teams (für eine mögliche Erweiterung der Applikation). */
    CLASSIC,
    /** Kooperativer Duett-Modus für zwei Spieler mit unabhängigen Keys. */
    DUET
}
