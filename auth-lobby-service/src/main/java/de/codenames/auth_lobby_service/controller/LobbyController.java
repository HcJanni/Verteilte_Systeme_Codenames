package de.codenames.auth_lobby_service.controller;

import de.codenames.auth_lobby_service.security.service.TokenService;
import de.codenames.auth_lobby_service.dto.request.CreateLobbyRequest;
import de.codenames.auth_lobby_service.dto.response.LobbyResponse;
import de.codenames.auth_lobby_service.model.Lobby;
import de.codenames.auth_lobby_service.model.LobbyStatus;
import de.codenames.auth_lobby_service.model.Player;
import de.codenames.auth_lobby_service.repository.LobbyRepository;
import de.codenames.auth_lobby_service.repository.PlayerRepository;
import de.codenames.auth_lobby_service.dto.response.PlayerResponse;
import de.codenames.auth_lobby_service.model.User;
import de.codenames.auth_lobby_service.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lobbies")
public class LobbyController {
    private final LobbyRepository lobbyRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;


    public LobbyController(LobbyRepository lobbyRepository, TokenService tokenService, UserRepository userRepository, PlayerRepository playerRepository) {
        this.lobbyRepository = lobbyRepository;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
    }

    @PostMapping
    public ResponseEntity<LobbyResponse> createLobby(@RequestHeader("Authorization") String token, @RequestBody CreateLobbyRequest request) {
        Optional<Long> foundUserId = tokenService.getUserId(token);
        if (foundUserId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Long userId = foundUserId.get();

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

    @PostMapping("/{lobbyId}/join")
    public ResponseEntity<PlayerResponse> joinLobby(@PathVariable Long lobbyId, @RequestHeader("Authorization") String token) {
        // 1. Token prüfen -> userId (wie bei createLobby, expliziter if/get-Stil)
        Optional<Long> foundUserId = tokenService.getUserId(token);
        if (foundUserId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Long userId = foundUserId.get();
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
}
