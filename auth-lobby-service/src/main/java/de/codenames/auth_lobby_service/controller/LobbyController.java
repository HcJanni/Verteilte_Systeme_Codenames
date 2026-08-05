package de.codenames.auth_lobby_service.controller;

import de.codenames.auth_lobby_service.dto.request.CreateLobbyRequest;
import de.codenames.auth_lobby_service.dto.request.ReportResultRequest;
import de.codenames.auth_lobby_service.dto.response.LobbyResponse;
import de.codenames.auth_lobby_service.model.*;
import de.codenames.auth_lobby_service.repository.GameHistoryRepository;
import de.codenames.auth_lobby_service.repository.LobbyRepository;
import de.codenames.auth_lobby_service.repository.PlayerRepository;
import de.codenames.auth_lobby_service.dto.response.PlayerResponse;
import de.codenames.auth_lobby_service.repository.UserRepository;
import de.codenames.auth_lobby_service.security.service.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// CrossOrigin von KI
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/lobbies")
public class LobbyController {
    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final GameHistoryRepository gameHistoryRepository;


    public LobbyController(LobbyRepository lobbyRepository, UserRepository userRepository, PlayerRepository playerRepository, GameHistoryRepository gameHistoryRepository) {
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.gameHistoryRepository = gameHistoryRepository;
    }

    @PostMapping
    public ResponseEntity<LobbyResponse> createLobby(Authentication authentication, @RequestBody CreateLobbyRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = foundUser.get();

        Lobby lobby = new Lobby();
        lobby.setName(request.name());
        lobby.setGameMode(request.gameMode());
        lobby.setStatus(LobbyStatus.OPEN);
        lobby.setCreatedBy(user);
        lobbyRepository.save(lobby);
        return ResponseEntity.ok(new LobbyResponse(lobby.getId(), lobby.getName(), lobby.getStatus(), lobby.getGameMode()));
    }

    @GetMapping
    public ResponseEntity<List<LobbyResponse>> listLobbies() {
        List<Lobby> lobbies = lobbyRepository.findByStatus(LobbyStatus.OPEN);
        List<LobbyResponse> response = new ArrayList<>();
        for (Lobby lobby : lobbies) {
            LobbyResponse lobbyResponse = new LobbyResponse(lobby.getId(), lobby.getName(), lobby.getStatus(), lobby.getGameMode());
            response.add(lobbyResponse);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{lobbyId}/players")
    public ResponseEntity<List<PlayerResponse>> getPlayers(@PathVariable Long lobbyId) {
        Optional<Lobby> foundLobby = lobbyRepository.findById(lobbyId);
        if (foundLobby.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<Player> players = playerRepository.findByLobby(foundLobby.get());
        List<PlayerResponse> response = new ArrayList<>();
        for (Player player : players) {
            PlayerResponse playerResponse = new PlayerResponse(player.getId(), player.getUser().getUsername(), player.getPlayerRole());
            response.add(playerResponse);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{lobbyId}/join")
    public ResponseEntity<PlayerResponse> joinLobby(@PathVariable Long lobbyId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        // 2. User laden (wie bei createLobby)
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = foundUser.get();
        // 3. Lobby laden über lobbyRepository.findById(lobbyId) — auch ein Optional, gleiche Behandlung
        Optional<Lobby> foundLobby = lobbyRepository.findById(lobbyId);
        if (foundLobby.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Lobby lobby = foundLobby.get();
        // 4. Neuen Player bauen: user, lobby, team=null, role=null
        Player player = new Player();
        player.setUser(user);
        player.setLobby(lobby);
        player.setPlayerRole(null);
        // 5. playerRepository.save(...)
        playerRepository.save(player);
        // 6. passenden Status zurückgeben
        return ResponseEntity.ok(new PlayerResponse(player.getId(), player.getUser().getUsername(), player.getPlayerRole()));
    }

    @PostMapping("/{lobbyId}/result")
    public ResponseEntity<Void> saveOutcome(@PathVariable Long lobbyId, @RequestBody ReportResultRequest request) {
        Optional<Lobby> foundLobby = lobbyRepository.findById(lobbyId);
        if (foundLobby.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        GameHistory gameHistory = new GameHistory();
        Lobby lobby = foundLobby.get();
        gameHistory.setLobby(lobby);
        gameHistory.setGameOutcome(request.outcome());
        gameHistory.setTurnCount(request.turnCount());
        gameHistory.setEndedAt(Instant.now());
        lobby.setStatus(LobbyStatus.FINISHED);
        lobbyRepository.save(lobby);
        gameHistoryRepository.save(gameHistory);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
