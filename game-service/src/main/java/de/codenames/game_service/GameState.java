package de.codenames.game_service;

import java.util.List;

public class GameState {
    private long lobbyId;
    private long player1Id;
    private long player2Id;
    private List<Card> cards;
    private int turnCount;
    private String currentClueWord;
    private Integer currentClueCount;

    public long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(long player1Id) {
        this.player1Id = player1Id;
    }

    public long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(long player2Id) {
        this.player2Id = player2Id;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public String getCurrentClueWord() {
        return currentClueWord;
    }
    public void setCurrentClueWord(String currentClueWord) {
        this.currentClueWord = currentClueWord;
    }
    public Integer getCurrentClueCount() {
        return currentClueCount;
    }
    public void setCurrentClueCount(Integer currentClueCount) {
        this.currentClueCount = currentClueCount;
    }
}
