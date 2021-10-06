package com.spring.kodowebrtc.handler;

import com.google.gson.Gson;
import com.spring.kodowebrtc.entity.SessionMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    Map<String, List<WebSocketSession>> sessions = new HashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException
    {
        System.out.println("Number of Sessions: " + sessions.size());

//        Gson gson = new Gson();
//        SessionMessage sessionMessage = gson.fromJson(new String(message.getPayload().getBytes(), UTF_8), SessionMessage.class);

        String sessionId = (String) session.getAttributes().get("sessionId");

        if (sessions.containsKey(sessionId))
        {
            for (WebSocketSession webSocketSession : sessions.get(sessionId)) {
                if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
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
        sessions.get(sessionId).add(session);

        System.out.println(session);
    }

    public void addNewSessionId(String sessionId)
    {
        this.sessions.put(sessionId, new CopyOnWriteArrayList<>());
        System.out.println(this.sessions);
    }
}