package de.codenames.auth_lobby_service.model;

/**
 * Lebenszyklus-Status einer {@link Lobby}.
 */
public enum LobbyStatus {
    /** Lobby ist offen, weitere Spieler können beitreten. */
    OPEN,
    /** Die Partie läuft bereits, kein Beitritt mehr möglich. */
    IN_PROGRESS,
    /** Die Partie ist beendet. */
    FINISHED
}
