package de.codenames.game_service;

import de.codenames.game_service.websocket.GameWebSocketHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// CrossOrigin von KI
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameStateService gameStateService;
    private final GameWebSocketHandler gameWebSocketHandler;
    private final RestClient restClient;

    public GameController(GameStateService gameStateService, GameWebSocketHandler gameWebSocketHandler) {
        this.gameStateService = gameStateService;
        this.gameWebSocketHandler = gameWebSocketHandler;
        this.restClient = RestClient.create("http://localhost:8081");
    }

    @PostMapping("/{lobbyId}/start")
    public ResponseEntity<Void> startGame(@PathVariable Long lobbyId, @RequestBody StartGameRequest request) {
        gameStateService.createGame(lobbyId, request.player1Id(), request.player2Id());
        gameWebSocketHandler.broadcast(lobbyId.toString());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{lobbyId}")
    public ResponseEntity<GameViewResponse> getGameView(@PathVariable Long lobbyId, @RequestParam Long viewerId) {
        Optional<GameState> foundGame = gameStateService.getGame(lobbyId);
        if (foundGame.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        GameState gameState = foundGame.get();

        if (viewerId != gameState.getPlayer1Id() && viewerId != gameState.getPlayer2Id()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<CardView> result = new ArrayList<>();
        for (Card card : gameState.getCards()) {
            CardType myType;
            CardType otherType;
            if (viewerId == gameState.getPlayer1Id()) {
                myType = card.getPlayer1Card();
                otherType = card.getPlayer2Card();
            } else {
                myType = card.getPlayer2Card();
                otherType = card.getPlayer1Card();
            }
            CardType otherTypeToShow = null;
            if (card.isRevealed()) {
                otherTypeToShow = otherType;
            }

            CardView cardView = new CardView(card.getWord(), card.getPosition(), card.isRevealed(), myType, otherTypeToShow);
            result.add(cardView);
        }
        return ResponseEntity.ok(new GameViewResponse(result, gameState.getTurnCount(), gameState.getCurrentClueWord(), gameState.getCurrentClueCount()));
    }

    @PostMapping("/{lobbyId}/reveal")
    public ResponseEntity<GameOutcome> reveal(@PathVariable Long lobbyId, @RequestBody RevealRequest request) {
        GameOutcome outcome = gameStateService.revealCard(lobbyId, request.position());
        gameWebSocketHandler.broadcast(lobbyId.toString());
        if (outcome != null) {
            Optional<GameState> foundGame = gameStateService.getGame(lobbyId);
            if (foundGame.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            GameState gameState = foundGame.get();
            int turnCount = gameState.getTurnCount();

            restClient.post()
                    .uri("/api/lobbies/{lobbyId}/result", lobbyId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ReportResultRequest(outcome, turnCount))
                    .retrieve()
                    .toBodilessEntity();
        }
        return ResponseEntity.ok(outcome);
    }

    @PostMapping("/{lobbyId}/clue")
    public ResponseEntity<Void> giveClue(@PathVariable Long lobbyId, @RequestBody ClueRequest request) {
        gameStateService.setClue(lobbyId, request.word(), request.count());
        // Wie beim Aufdecken senden wir einen Broadcast, damit das Frontend sich aktualisiert
        gameWebSocketHandler.broadcast(lobbyId.toString());
        return ResponseEntity.ok().build();
    }
}