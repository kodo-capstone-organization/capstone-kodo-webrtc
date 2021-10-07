package com.spring.kodowebrtc.handler;

import com.spring.kodowebrtc.restentity.request.CreateSessionReq;
import com.spring.kodowebrtc.restentity.request.UserInfoReq;
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

    public void addInvitationForInvitees(CreateSessionReq createSessionReq)
    {
        for (UserInfoReq userInfo : createSessionReq.getInviteeInfos())
        {
            List<InvitedSessionResp> invitedSessionReps = userInvitations.getOrDefault(userInfo.getUserId(), new CopyOnWriteArrayList<>());

            InvitedSessionResp invitedSessionResp = new InvitedSessionResp();

            invitedSessionResp.setHostId(createSessionReq.getCreatorInfo().getUserId());
            invitedSessionResp.setSessionId(createSessionReq.getSessionId());
            invitedSessionResp.setSessionName(createSessionReq.getSessionName());

            invitedSessionReps.add(invitedSessionResp);

            userInvitations.put(userInfo.getUserId(), invitedSessionReps);
        }
    }

    public List<InvitedSessionResp> getInvitedSessionRespForUser(Long inviteeId)
    {
        return userInvitations.getOrDefault(inviteeId, new CopyOnWriteArrayList<>());
    }

    public void deleteInvitationsBySessionId(String sessionId)
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
