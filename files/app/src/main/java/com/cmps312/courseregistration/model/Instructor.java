package com.cmps312.courseregistration.model;

import java.util.ArrayList;
import java.util.Date;

public class Instructor extends QatarUniMember {

    private ArrayList<String> qualifications;
    private String title;
    private String type;

    public Instructor() {
    }

    public Instructor(ArrayList<String> qualifications, String title, String type) {
        this.qualifications = qualifications;
        this.title = title;
        this.type = type;
    }

    public ArrayList<String> getQualifications() {
        return qualifications;
    }

    public void setQualifications(ArrayList<String> qualifications) {
        this.qualifications = qualifications;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
