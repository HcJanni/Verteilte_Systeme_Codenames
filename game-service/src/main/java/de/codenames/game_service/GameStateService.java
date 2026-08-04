package de.codenames.game_service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameStateService {
    private Map<Long, GameState> games = new ConcurrentHashMap<>();
    private static final List<String> word_pool = List.of("Apfel", "Berg", "Katze", "Draht", "Elefant", "Fluss", "Fenster", "Hammer", "Insel", "Jacke",
            "Kerze", "Lampe", "Messer", "Nadel", "Orange", "Pferd", "Quelle", "Reifen", "Sonne", "Teller",
            "Uhr", "Vogel", "Wald", "Zug", "Brille", "Decke", "Fahrrad", "Gabel", "Haus", "Kissen",
            "Löwe", "Mond", "Nebel", "Ofen", "Pinsel", "Rakete", "Schlüssel", "Tasche", "Vulkan", "Wolke",
            "Zahn", "Anker", "Besen", "Diamant", "Feder", "Garten", "Helm", "Kompass", "Leiter", "Spiegel");

    public GameState createGame(long lobbyId, long player1Id, long player2Id) {
        List<String> words = new ArrayList<>(word_pool);
        Collections.shuffle(words);
        List<CardType> player1Words = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            player1Words.add(i, CardType.AGENT);
        }
        player1Words.add(9, CardType.ASSASSIN);
        for (int i = 10; i < 25; i++) {
            player1Words.add(i, CardType.BYSTANDER);
        }

        List<CardType> player2Words = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            player2Words.add(i, CardType.AGENT);
        }
        player2Words.add(9, CardType.ASSASSIN);
        for (int i = 10; i < 25; i++) {
            player2Words.add(i, CardType.BYSTANDER);
        }

        Collections.shuffle(player1Words);
        Collections.shuffle(player2Words);

        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            Card card = new Card();
            card.setWord(words.get(i));
            card.setPosition(i);
            card.setRevealed(false);
            card.setPlayer1Card(player1Words.get(i));
            card.setPlayer2Card(player2Words.get(i));
            cards.add(card);
        }

        GameState gameState = new GameState();
        gameState.setLobbyId(lobbyId);
        gameState.setPlayer1Id(player1Id);
        gameState.setPlayer2Id(player2Id);
        gameState.setCards(cards);
        gameState.setTurnCount(0);

        games.put(lobbyId, gameState);
        return gameState;
    }

    public Optional<GameState> getGame(Long lobbyId) {
        return Optional.ofNullable(games.get(lobbyId));
    }

    public GameOutcome revealCard(Long lobbyId, int position) {
        Optional<GameState> foundGame = getGame(lobbyId);
        if (foundGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        GameState gameState = foundGame.get();

        Card revealedCard = null;
        for (Card card : gameState.getCards()) {
            if (card.getPosition() == position) {
                card.setRevealed(true);
                revealedCard = card;
            }
        }
        gameState.setTurnCount(gameState.getTurnCount() + 1);

        if (revealedCard.getPlayer1Card() == CardType.ASSASSIN || revealedCard.getPlayer2Card() == CardType.ASSASSIN) {
            return GameOutcome.ASSASSIN_LOSS;
        }
        boolean allAgentsRevealed = true;
        for (Card card : gameState.getCards()) {
            if (!card.isRevealed() && (card.getPlayer1Card() == CardType.AGENT || card.getPlayer2Card() == CardType.AGENT)) {
                allAgentsRevealed = false;
            }
        }
        if (allAgentsRevealed) {
            return GameOutcome.WIN;
        }
        return null;
    }
}
