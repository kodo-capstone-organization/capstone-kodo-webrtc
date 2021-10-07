package com.spring.kodowebrtc.restentity.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InvitedSessionResp
{
    private String sessionName;

    private String sessionId;

    private Long hostId;

    @JsonIgnore
    private LocalDateTime dateTimeOfSessionCreation;

    public InvitedSessionResp() {
        dateTimeOfSessionCreation = LocalDateTime.now();
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

    public LocalDateTime getDateTimeOfSessionCreation() {
        return dateTimeOfSessionCreation;
    }

    public void setDateTimeOfSessionCreation(LocalDateTime dateTimeOfSessionCreation) {
        this.dateTimeOfSessionCreation = dateTimeOfSessionCreation;
    }
}
