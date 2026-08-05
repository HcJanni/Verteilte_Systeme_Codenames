package de.codenames.auth_lobby_service.dto.request;

import de.codenames.auth_lobby_service.model.GameOutcome;

public record ReportResultRequest(GameOutcome outcome, int turnCount) {}
