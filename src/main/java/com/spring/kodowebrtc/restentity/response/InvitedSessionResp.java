package com.spring.kodowebrtc.restentity.response;

public class InvitedSessionResp
{
    private String sessionName;

    private String sessionId;

    private Long hostId;

    public InvitedSessionResp() {
    }

    public InvitedSessionResp(String sessionName, String sessionId, Long hostId) {
        this.sessionName = sessionName;
        this.sessionId = sessionId;
        this.hostId = hostId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }
}
