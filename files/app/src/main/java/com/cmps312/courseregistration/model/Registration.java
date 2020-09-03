package com.cmps312.courseregistration.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

public class Registration implements Parcelable {
    private Date dateOfRegistration;
    private String grade;
    private String status;
    private CourseSchedule courseSchedule;
    private String semester;
    private int year;
    private Fee fee;


    public Registration() {}

    public Registration( String grade, String status, CourseSchedule courseSchedule) {
        this.grade = grade;
        this.status = status;
        this.courseSchedule = courseSchedule;
        this.semester = courseSchedule.getSemester();
        this.year = courseSchedule.getYear();
        this.dateOfRegistration = Calendar.getInstance().getTime();
        fee = new Fee(false,null, courseSchedule.getCourseObj().getCreditHours()*1000);
    }



    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public CourseSchedule getCourseSchedule() {
        return courseSchedule;
    }

    public void setCourseSchedule(CourseSchedule courseSchedule) {
        this.courseSchedule = courseSchedule;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }


    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.dateOfRegistration != null ? this.dateOfRegistration.getTime() : -1);
        dest.writeString(this.grade);
        dest.writeString(this.status);
        dest.writeParcelable(this.courseSchedule, flags);
        dest.writeString(this.semester);
        dest.writeInt(this.year);
        dest.writeParcelable(this.fee, flags);
    }

    protected Registration(Parcel in) {
        long tmpDateOfRegistration = in.readLong();
        this.dateOfRegistration = tmpDateOfRegistration == -1 ? null : new Date(tmpDateOfRegistration);
        this.grade = in.readString();
        this.status = in.readString();
        this.courseSchedule = in.readParcelable(CourseSchedule.class.getClassLoader());
        this.semester = in.readString();
        this.year = in.readInt();
        this.fee = in.readParcelable(Fee.class.getClassLoader());
    }

    public static final Creator<Registration> CREATOR = new Creator<Registration>() {
        @Override
        public Registration createFromParcel(Parcel source) {
            return new Registration(source);
        }

        @Override
        public Registration[] newArray(int size) {
            return new Registration[size];
        }
    };
}
