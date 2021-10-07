package com.spring.kodowebrtc.controller;

import com.spring.kodowebrtc.handler.SocketHandler;
import com.spring.kodowebrtc.util.CryptographicHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/kodoSession")
public class SessionController
{
    @Autowired
    private SocketHandler socketHandler;

    @GetMapping("/createSession/{sessionName}")
    public String createSession(@PathVariable String sessionName)
    {
        String salt = CryptographicHelper.generateRandomString(64);
        String sessionId = CryptographicHelper.getSHA256Digest(sessionName, salt);

        socketHandler.addNewSessionId(sessionId);

        return sessionId;
    }
}