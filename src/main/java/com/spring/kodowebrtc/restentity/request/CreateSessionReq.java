package com.spring.kodowebrtc.restentity.request;

import java.util.List;

public class CreateSessionReq
{
    private String sessionName;

    private String sessionId;

    private UserInfoReq creatorInfo;

    private List<UserInfoReq> inviteeInfos;

    public CreateSessionReq() {
    }

    public CreateSessionReq(String sessionName, String sessionId, UserInfoReq creatorInfo, List<UserInfoReq> inviteeInfos) {
        this.sessionName = sessionName;
        this.sessionId = sessionId;
        this.creatorInfo = creatorInfo;
        this.inviteeInfos = inviteeInfos;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public UserInfoReq getCreatorInfo() {
        return creatorInfo;
    }

    public void setCreatorInfo(UserInfoReq creatorInfo) {
        this.creatorInfo = creatorInfo;
    }

    public List<UserInfoReq> getInviteeInfos() {
        return inviteeInfos;
    }

    public void setInviteeInfos(List<UserInfoReq> inviteeInfos) {
        this.inviteeInfos = inviteeInfos;
    }
}
