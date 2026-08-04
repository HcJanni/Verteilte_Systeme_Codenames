package de.codenames.auth_lobby_service.model;

/**
 * Rolle eines Spielers innerhalb einer Partie (nur für den Classic-Modus relevant).
 * <p>
 * Im Duett-Modus wird diese Rolle nicht vergeben ({@code playerRole} bleibt {@code null}),
 * da dort jeder Spieler gleichzeitig Hinweisgeber und Rater ist.
 */
public enum PlayerRole {
    /** Sieht den kompletten Key des Teams. */
    SPYMASTER,
    /** Sieht nur die Wörter, nicht deren geheime Zuordnung. */
    OPERATIVE
}
