package de.codenames.auth_lobby_service.security.service;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Verwaltet Login-Sessions über einfache, zufällige Tokens im Arbeitsspeicher.
 * <p>
 * Bewusst vereinfachte Alternative zu Spring Security/JWT, Tokens gehen bei
 * einem Server-Neustart verloren.
 */
@Component
public class TokenService {
    /** Token -> zugehörige User-ID. */
    private final Map<String, Long> sessionTokens = new ConcurrentHashMap<>();

    /**
     * Erzeugt einen neuen, zufälligen Token für einen erfolgreich angemeldeten Nutzer.
     *
     * @param userId die ID des angemeldeten Nutzers
     * @return der neu erzeugte Token
     */
    public String createToken(long userId) {
        String token = UUID.randomUUID().toString();
        sessionTokens.put(token, userId);
        return token;
    }

    /**
     * Löst einen Token zur zugehörigen User-ID auf.
     *
     * @param token der zu prüfende Token
     * @return die zugehörige User-ID, oder ein leeres Optional, falls der Token unbekannt/ungültig ist
     */
    public Optional<Long> getUserId(String token) {
        return Optional.ofNullable(sessionTokens.get(token));
    }
}
