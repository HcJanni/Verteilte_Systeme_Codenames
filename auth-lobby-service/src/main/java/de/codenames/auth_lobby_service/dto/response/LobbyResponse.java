package de.codenames.auth_lobby_service.dto.response;

import de.codenames.auth_lobby_service.model.GameMode;
import de.codenames.auth_lobby_service.model.LobbyStatus;

public record LobbyResponse(Long id, String name, LobbyStatus status, GameMode gameMode) {}
