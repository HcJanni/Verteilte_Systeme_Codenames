package de.codenames.game_service;

public record ReportResultRequest(GameOutcome outcome, int turnCount) {}