package de.codenames.game_service;

import java.util.List;

public record GameViewResponse(List<CardView> cards, int turnCount, String currentClueWord, Integer currentClueCount) {

}