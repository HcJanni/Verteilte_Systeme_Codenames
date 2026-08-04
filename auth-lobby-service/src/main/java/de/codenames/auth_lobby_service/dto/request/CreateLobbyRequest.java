package de.codenames.auth_lobby_service.dto.request;

import de.codenames.auth_lobby_service.model.GameMode;

public record CreateLobbyRequest(String name, GameMode gameMode) {}
