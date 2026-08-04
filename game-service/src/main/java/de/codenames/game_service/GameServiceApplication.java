package de.codenames.game_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameServiceApplication.class, args);
		GameStateService gameStateService = new GameStateService();
		GameState gameState = gameStateService.createGame(1L, 1L, 2L);
		for(Card card : gameState.getCards()) {
			System.out.println(card.getWord() + " - Spieler1: " + card.getPlayer1Card() + " - Spieler2: " + card.getPlayer2Card());
		}
	}

}
