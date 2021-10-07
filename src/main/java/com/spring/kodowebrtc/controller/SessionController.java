package com.spring.kodowebrtc.controller;

import com.spring.kodowebrtc.handler.SocketHandler;
import com.spring.kodowebrtc.restentity.request.CreateSessionReq;
import com.spring.kodowebrtc.util.CryptographicHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/kodoSession")
public class SessionController
{
    @Autowired
    private SocketHandler socketHandler;

    @PostMapping("/createSession")
    public String createSession(@RequestPart(name = "createSessionReq", required = true) CreateSessionReq createSessionReq)
    {
        String salt = CryptographicHelper.generateRandomString(64);
        String sessionId = CryptographicHelper.getSHA256Digest(createSessionReq.getSessionName(), salt);

        socketHandler.addNewSessionId(sessionId);

        return sessionId;
    }
}