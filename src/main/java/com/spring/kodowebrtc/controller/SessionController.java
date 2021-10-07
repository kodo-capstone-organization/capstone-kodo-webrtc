package com.spring.kodowebrtc.controller;

import com.spring.kodowebrtc.handler.InvitationHandler;
import com.spring.kodowebrtc.handler.SocketHandler;
import com.spring.kodowebrtc.restentity.request.CreateSessionReq;
import com.spring.kodowebrtc.restentity.response.InvitedSessionResp;
import com.spring.kodowebrtc.util.CryptographicHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/kodoSession")
public class SessionController
{
    @Autowired
    private SocketHandler socketHandler;

    @Autowired
    private InvitationHandler invitationHandler;

    @PostMapping("/createSession")
    public String createSession(@RequestPart(name = "createSessionReq", required = true) CreateSessionReq createSessionReq)
    {
        String salt = CryptographicHelper.generateRandomString(64);
        String sessionId = CryptographicHelper.getSHA256Digest(createSessionReq.getSessionName(), salt);

        this.socketHandler.addNewSessionId(sessionId);
        this.invitationHandler.addInvitationForInvitees(createSessionReq);

        return sessionId;
    }

    @GetMapping("/getInvitedSessions/{userId}")
    public List<InvitedSessionResp> getInvitedSessions(@PathVariable(name = "userId", required = true) Long userId)
    {
        return invitationHandler.getInvitedSessionRespForUser(userId);
    }

    @DeleteMapping("/endSession/{sessionId}")
    public void endSession(@PathVariable(name = "sessionId", required = true) String sessionId)
    {
        this.socketHandler.deleteSessionId(sessionId);
        this.invitationHandler.deleteInvitationsBySessionId(sessionId);
    }
}