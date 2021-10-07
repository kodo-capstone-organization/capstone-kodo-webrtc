package com.spring.kodowebrtc.handler;

import com.spring.kodowebrtc.restentity.request.CreateSessionReq;
import com.spring.kodowebrtc.restentity.request.UserInfoReq;
import com.spring.kodowebrtc.restentity.response.InvitedSessionResp;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InvitationHandler
{
    Map<Long, List<InvitedSessionResp>> userInvitations = Collections.synchronizedMap(new HashMap<>());

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
}
