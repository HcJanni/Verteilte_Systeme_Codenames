package de.codenames.game_service;

public class Card {
    private String word;
    private int position;
    private boolean revealed;
    private CardType player1Card;
    private CardType player2Card;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public CardType getPlayer1Card() {
        return player1Card;
    }

    public void setPlayer1Card(CardType player1Card) {
        this.player1Card = player1Card;
    }

    public CardType getPlayer2Card() {
        return player2Card;
    }

    public void setPlayer2Card(CardType player2Card) {
        this.player2Card = player2Card;
    }
}
