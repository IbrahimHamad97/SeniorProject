package com.cmps312.courseregistration.model;

import java.util.ArrayList;
import java.util.Date;

public class Advisor extends QatarUniMember {
    private String supervisor;
    private String advisedMajor;

    public Advisor(String supervisor, String advisedMajor) {
        this.supervisor = supervisor;
        this.advisedMajor = advisedMajor;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getAdvisedMajor() {
        return advisedMajor;
    }

    public void setAdvisedMajor(String advisedMajor) {
        this.advisedMajor = advisedMajor;
    }
}
