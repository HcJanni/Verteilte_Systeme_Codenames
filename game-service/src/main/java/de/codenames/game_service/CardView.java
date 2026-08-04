package de.codenames.game_service;

public record CardView(String word, int position, boolean revealed, CardType myType, CardType otherType) {
}
