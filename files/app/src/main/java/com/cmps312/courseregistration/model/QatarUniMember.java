package com.cmps312.courseregistration.model;

import java.util.Date;

public abstract class QatarUniMember{
    private int QUID;
    private String firstName;
    private String lastName;
    private Date dob;
    private String gender;
    private String email;
    private String phoneNo;
    private String college;

    public QatarUniMember(){}

    public QatarUniMember(int QUID, String firstName, String lastName, String gender, Date dob, String email, String phoneNo, String college) {
        this.QUID = QUID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.phoneNo = phoneNo;
        this.college = college;
    }


    public int getQUID() {
        return QUID;
    }

    public void setQUID(int QUID) {
        this.QUID = QUID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
