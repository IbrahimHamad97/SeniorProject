package com.cmps312.courseregistration.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class Course implements Parcelable {
    private int code;
    private String id;
    private String attribute;
    private String courseName;
    private int creditHours;
    private String preRequisiteCourse1;
    private String preRequisiteCourse2;
    private String preRequisiteCourse3;
    private String subject;
    private int linkedCourse;
    private Boolean lab;

    public Course(){

    }

    public Course(int code, String attribute, String courseName, int creditHours, String preRequisiteCourse1, String preRequisiteCourse2, String preRequisiteCourse3, String subject, int linkedCourse, Boolean lab) {
        this.code = code;
        this.attribute = attribute;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.preRequisiteCourse1 = preRequisiteCourse1;
        this.preRequisiteCourse2 = preRequisiteCourse2;
        this.preRequisiteCourse3 = preRequisiteCourse3;
        this.subject = subject;
        this.linkedCourse = linkedCourse;
        this.lab = lab;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public String getPreRequisiteCourse1() {
        return preRequisiteCourse1;
    }

    public void setPreRequisiteCourse1(String preRequisiteCourse1) {
        this.preRequisiteCourse1 = preRequisiteCourse1;
    }

    public String getPreRequisiteCourse2() {
        return preRequisiteCourse2;
    }

    public void setPreRequisiteCourse2(String preRequisiteCourse2) {
        this.preRequisiteCourse2 = preRequisiteCourse2;
    }

    public String getPreRequisiteCourse3() {
        return preRequisiteCourse3;
    }

    public void setPreRequisiteCourse3(String preRequisiteCourse3) {
        this.preRequisiteCourse3 = preRequisiteCourse3;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getLinkedCourse() {
        return linkedCourse;
    }

    public void setLinkedCourse(int linkedCourse) {
        this.linkedCourse = linkedCourse;
    }

    public Boolean getLab() {
        return lab;
    }

    public void setLab(Boolean lab) {
        this.lab = lab;
    }

    public String getCourseCodeLabel() {
        return String.format(Locale.US,"%s\n%d", subject, code);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.id);
        dest.writeString(this.attribute);
        dest.writeString(this.courseName);
        dest.writeInt(this.creditHours);
        dest.writeString(this.preRequisiteCourse1);
        dest.writeString(this.preRequisiteCourse2);
        dest.writeString(this.preRequisiteCourse3);
        dest.writeString(this.subject);
        dest.writeInt(this.linkedCourse);
        dest.writeValue(this.lab);
    }

    protected Course(Parcel in) {
        this.code = in.readInt();
        this.id = in.readString();
        this.attribute = in.readString();
        this.courseName = in.readString();
        this.creditHours = in.readInt();
        this.preRequisiteCourse1 = in.readString();
        this.preRequisiteCourse2 = in.readString();
        this.preRequisiteCourse3 = in.readString();
        this.subject = in.readString();
        this.linkedCourse = in.readInt();
        this.lab = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
}
