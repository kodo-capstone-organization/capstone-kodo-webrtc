package com.spring.kodowebrtc.restentity.request;

import java.util.List;

public class CreateSessionReq
{
    private String sessionName;
    private Boolean isPublic;
    private Long creatorId;
    private List<Long> inviteeIds;

    public CreateSessionReq() {
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public List<Long> getInviteeIds() {
        return inviteeIds;
    }

    public void setInviteeIds(List<Long> inviteeIds) {
        this.inviteeIds = inviteeIds;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean aPublic) {
        isPublic = aPublic;
    }
}
