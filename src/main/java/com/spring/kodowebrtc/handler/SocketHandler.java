package com.spring.kodowebrtc.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler
{

    Map<String, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException
    {
        String sessionId = (String) session.getAttributes().get("sessionId");

        if (sessions.containsKey(sessionId))
        {
            for (WebSocketSession webSocketSession : sessions.get(sessionId))
            {
                if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId()))
                {
                    webSocketSession.sendMessage(message);
                }
            }
        }
        else
        {
            throw new InterruptedException("Invalid session id");
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        String sessionId = (String) session.getAttributes().get("sessionId");

        if (!sessions.containsKey(sessionId))
        {
            throw new Exception("Invalid session id");
        }

        sessions.get(sessionId).add(session);
    }

    public void addNewSessionId(String sessionId)
    {
        this.sessions.put(sessionId, new CopyOnWriteArrayList<>());
    }


    public boolean checkHasWebSocketSession(String sessionId)
    {
        return sessions.containsKey(sessionId);
    }

    public void deleteSessionId(String sessionId)
    {
        this.sessions.remove(sessionId);
    }
}