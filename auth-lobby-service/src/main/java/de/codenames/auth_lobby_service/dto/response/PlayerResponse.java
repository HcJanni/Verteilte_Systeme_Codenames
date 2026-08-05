package de.codenames.auth_lobby_service.dto.response;

import de.codenames.auth_lobby_service.model.PlayerRole;

/**
 * Antwort-DTO für den Lobby-Beitritt, enthält nur die für den Client relevanten Felder,
 * nicht das komplette {@link de.codenames.auth_lobby_service.model.Player}-Objekt
 * (vermeidet, sensible {@link de.codenames.auth_lobby_service.model.User}-Daten wie
 * den Passwort-Hash über die {@code createdBy}/{@code user}-Beziehung mit auszuliefern).
 */
public record PlayerResponse(Long id, Long userId, String username, PlayerRole playerRole) {}
