package de.codenames.game_service.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameWebSocketHandler extends TextWebSocketHandler {
    private CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        for(WebSocketSession s : sessions) {
            s.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    public void broadcast(String message) {
        for(WebSocketSession s : sessions) {
            try {
                s.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
