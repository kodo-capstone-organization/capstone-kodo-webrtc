package com.spring.kodowebrtc.handler;

import com.spring.kodowebrtc.restentity.request.CreateSessionReq;
import com.spring.kodowebrtc.restentity.response.InvitedSessionResp;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InvitationHandler
{
    Map<Long, List<InvitedSessionResp>> userInvitations = new ConcurrentHashMap<>();

    Map<String, InvitedSessionResp> publicSessions = new ConcurrentHashMap<>();

    public void addInvitationForInvitees(CreateSessionReq createSessionReq, String generatedSessionId)
    {
        for (Long userId : createSessionReq.getInviteeIds())
        {
            List<InvitedSessionResp> invitedSessionReps = userInvitations.getOrDefault(userId, new CopyOnWriteArrayList<>());

            InvitedSessionResp invitedSessionResp = new InvitedSessionResp();
            invitedSessionResp.setSessionId(generatedSessionId);
            invitedSessionResp.setHostId(createSessionReq.getCreatorId());
            invitedSessionResp.setSessionName(createSessionReq.getSessionName());
            invitedSessionReps.add(invitedSessionResp);

            userInvitations.put(userId, invitedSessionReps);
        }
    }

    public void addNewPublicSession(CreateSessionReq createSessionReq, String generatedSessionId)
    {
        InvitedSessionResp invitedSessionResp = new InvitedSessionResp();
        invitedSessionResp.setSessionId(generatedSessionId);
        invitedSessionResp.setHostId(createSessionReq.getCreatorId());
        invitedSessionResp.setSessionName(createSessionReq.getSessionName());
        publicSessions.put(generatedSessionId, invitedSessionResp);
    }

    public List<InvitedSessionResp> getInvitedSessionRespForUser(Long inviteeId)
    {
        return userInvitations.getOrDefault(inviteeId, new CopyOnWriteArrayList<>());
    }

    public void deleteInvitationsBySessionId(String sessionId)
    {
        if (isSessionPublic(sessionId))
        {
            publicSessions.remove(sessionId);
        }
        else
        {
            Map<Long, List<InvitedSessionResp>> updatedUserInvitations = Collections.synchronizedMap(new HashMap<>());
            for (Map.Entry<Long, List<InvitedSessionResp>> entry : userInvitations.entrySet())
            {
                List<InvitedSessionResp> updatedSessionResps = new CopyOnWriteArrayList<>();
                for (InvitedSessionResp invitedSessionResp : entry.getValue())
                {
                    if (!invitedSessionResp.getSessionId().equals(sessionId))
                    {
                        updatedSessionResps.add(invitedSessionResp);
                    }
                }
                updatedUserInvitations.put(entry.getKey(), updatedSessionResps);
            }
            userInvitations = updatedUserInvitations;
        }
    }

    public boolean isUserInvited(String sessionId, Long userId)
    {
        List<InvitedSessionResp> userInvitations = getInvitedSessionRespForUser(userId);
        return userInvitations.stream().anyMatch(ui -> ui.getSessionId().equals(sessionId));
    }

    public boolean isSessionPublic(String sessionId)
    {
        return publicSessions.containsKey(sessionId);
    }

    public InvitedSessionResp getInvitedSession(String sessionId, Long userId)
    {
        List<InvitedSessionResp> userInvitations = getInvitedSessionRespForUser(userId);
        return userInvitations.stream().filter(ui -> ui.getSessionId().equals(sessionId)).findFirst().orElse(null);
    }

    public InvitedSessionResp getPublicSession(String sessionId)
    {
        return publicSessions.get(sessionId);
    }

    // Scheduled to run every hour
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void scheduleDeleteInvitationBySessionId()
    {
        System.out.println("Deleting expired invitations");

        Map<Long, List<InvitedSessionResp>> updatedUserInvitations = Collections.synchronizedMap(new HashMap<>());
        for (Map.Entry<Long, List<InvitedSessionResp>> entry : userInvitations.entrySet())
        {
            List<InvitedSessionResp> updatedSessionResps = new CopyOnWriteArrayList<>();
            for (InvitedSessionResp invitedSessionResp : entry.getValue())
            {
                // Check whether invite is older than 12 hours
                if (!invitedSessionResp.getDateTimeOfSessionCreation().isBefore(LocalDateTime.now().minusHours(12L)) )
                {
                    updatedSessionResps.add(invitedSessionResp);
                }
            }
            updatedUserInvitations.put(entry.getKey(), updatedSessionResps);
        }
        userInvitations = updatedUserInvitations;
    }
}
