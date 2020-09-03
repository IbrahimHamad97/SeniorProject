package com.cmps312.courseregistration.model;

import java.util.Date;

public class OverrideRequest {
    private String advisor;
    private String reason;
    private Date submittedDate;
    private String requesterName;
    private int requesterId;

    public OverrideRequest(String advisor, String reason, Date submittedDate, String requesterName, int requesterId) {
        this.advisor = advisor;
        this.reason = reason;
        this.submittedDate = submittedDate;
        this.requesterName = requesterName;
        this.requesterId = requesterId;
    }

    public String getAdvisor() {
        return advisor;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(int requesterId) {
        this.requesterId = requesterId;
    }
}
