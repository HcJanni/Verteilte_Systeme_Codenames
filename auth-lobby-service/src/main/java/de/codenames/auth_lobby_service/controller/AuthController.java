package de.codenames.auth_lobby_service.controller;

import de.codenames.auth_lobby_service.dto.request.LoginRequest;
import de.codenames.auth_lobby_service.dto.request.RegisterRequest;
import de.codenames.auth_lobby_service.dto.response.AuthResponse;
import de.codenames.auth_lobby_service.model.User;
import de.codenames.auth_lobby_service.repository.UserRepository;
import de.codenames.auth_lobby_service.security.jwt.JwtUtils;
import de.codenames.auth_lobby_service.security.service.UserDetailsImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-Endpunkte für Registrierung und Login.
 * <p>
 * Verwaltet keine eigene Session-Logik selbst, sondern delegiert das Token-Handling
 * an {@link TokenService}, bewusst einfach gehalten anstelle von vollständiger
 * Spring Security (Begründung siehe Projekt-Dokumentation, Kapitel Realisierung).
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

        return ResponseEntity.ok().body(new AuthResponse(token, userDetails.getId()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (userRepository.existsByEmail(registerRequest.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Create new user's account
        User user = new User(registerRequest.username(),
                registerRequest.email(),
                encoder.encode(registerRequest.password()));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}