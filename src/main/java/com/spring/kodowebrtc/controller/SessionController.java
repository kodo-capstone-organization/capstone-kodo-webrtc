package com.spring.kodowebrtc.controller;

import com.spring.kodowebrtc.handler.InvitationHandler;
import com.spring.kodowebrtc.handler.SocketHandler;
import com.spring.kodowebrtc.restentity.request.CreateSessionReq;
import com.spring.kodowebrtc.restentity.response.InvitedSessionResp;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        String randomGeneratedValue = RandomStringUtils.randomNumeric(10);
        String sessionId = randomGeneratedValue.substring(0, 3) + " " + randomGeneratedValue.substring(3, 6) + " " + randomGeneratedValue.substring(6);

        this.socketHandler.addNewSessionId(sessionId);

        if (!createSessionReq.getIsPublic())
        {
            this.invitationHandler.addInvitationForInvitees(createSessionReq, sessionId);
        }
        else
        {
            this.invitationHandler.addNewPublicSession(createSessionReq, sessionId);
        }

        return sessionId;
    }

    @GetMapping("/getSessionBySessionId/{sessionId}&{userId}")
    public InvitedSessionResp getSessionBySessionId(@PathVariable(name="sessionId", required=true) String sessionId, @PathVariable(name="userId", required=true) Long userId)
    {
        if (socketHandler.checkHasWebSocketSession(sessionId))
        {
            if (invitationHandler.isUserInvited(sessionId, userId))
            {
                return invitationHandler.getInvitedSession(sessionId, userId);
            }
            else if (invitationHandler.isSessionPublic(sessionId))
            {
                return invitationHandler.getPublicSession(sessionId);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not invited to this session");
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such session found");
        }
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
        this.invitationHandler.deleteInvitationsBySessionId(sessionId); // handles deletion of public and private sessions
    }
}