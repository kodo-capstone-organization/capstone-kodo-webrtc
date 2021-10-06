package com.spring.kodowebrtc.entity;

public class SessionMessage {

    private String sessionId;

    private String event;

    private Object data;

    public SessionMessage() {
    }

    public SessionMessage(String sessionId, String event, Object data) {
        this.sessionId = sessionId;
        this.event = event;
        this.data = data;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
