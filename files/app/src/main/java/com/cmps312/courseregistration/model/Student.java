package com.cmps312.courseregistration.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Student extends QatarUniMember implements Parcelable {

    private int enrolledYear;
    private int currSemester;
    private double cummGPA;
    private String status;
    private String major;
    private String minor;
    private ArrayList<Registration> registrations;

    public Student() {}

    public Student(int QUID, String firstName, String lastName, String gender, Date dob, String email, String phoneNo, String college,int enrolledYear, int currSemester, double cummGPA, String status, String major,
                   String minor) {
        super(QUID,firstName,lastName,gender,dob,email,phoneNo,college);
        this.enrolledYear = enrolledYear;
        this.currSemester = currSemester;
        this.cummGPA = cummGPA;
        this.status = status;
        this.major = major;
        this.minor = minor;
        registrations = new ArrayList<>();
    }

    public ArrayList<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(ArrayList<Registration> registrations) {
        this.registrations = registrations;
    }

    public int getEnrolledYear() {
        return enrolledYear;
    }

    public void setEnrolledYear(int enrolledYear) {
        this.enrolledYear = enrolledYear;
    }

    public int getCurrSemester() {
        return currSemester;
    }

    public void setCurrSemester(int currSemester) {
        this.currSemester = currSemester;
    }

    public double getCummGPA() {
        return cummGPA;
    }

    public void setCummGPA(double cummGPA) {
        this.cummGPA = cummGPA;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public boolean registerCourse(CourseSchedule courseSched) {

        if(checkRegistered(courseSched)==false && checkPrequisite(courseSched) && !courseSched.isFull()) {
            Registration tempRegistration = new Registration("UF", "Registered", courseSched);
            registrations.add(tempRegistration);
            return true;
        }
        return false;

    }

    public boolean registerWaitingList(CourseSchedule courseSched) {

        if(checkRegistered(courseSched)==false && checkPrequisite(courseSched)) {
            Registration tempRegistration = new Registration("UF", "Waiting List", courseSched);
            registrations.add(tempRegistration);
            return true;
        }
        return false;

    }

    public String checkTimeConflict(CourseSchedule sched){
        for (Registration registration: registrations
        ) {
            for(SchedGroup schedGroup:registration.getCourseSchedule().getSchedGroups()){
                for(SchedGroup schedGroup1:sched.getSchedGroups()){
                    if(!Collections.disjoint(schedGroup1.getDays(),schedGroup.getDays())){
                        if(schedGroup1.getStartTime()>=schedGroup.getStartTime() && schedGroup1.getStartTime()<=schedGroup.getEndTime())
                            return registration.getCourseSchedule().getCrn();
                    }
                }
            }
        }
        return "-1";
    }

    public boolean checkPrequisite(CourseSchedule sched){

        Map<String, Boolean> map = new HashMap<>();
        String prereq1 = sched.getCourseObj().getPreRequisiteCourse1();
        String prereq2 = sched.getCourseObj().getPreRequisiteCourse2();
        String prereq3 = sched.getCourseObj().getPreRequisiteCourse3();
        map.put(prereq1,true);
        map.put(prereq2,true);
        map.put(prereq3,true);
        if(!prereq1.equals("null")){
            map.put(prereq1,false);
        }if(!prereq2.equals("null")){
            map.put(prereq2,false);
        }
        if(!prereq3.equals("null")){
            map.put(prereq3,false);
        }
        for (Registration registration: registrations
        ) {
            String courseId = registration.getCourseSchedule().getCourse();
            if(courseId.equals(prereq1)){
                map.put(prereq1,true);
            }
            if(courseId.equals(prereq2)){
                map.put(prereq2,true);
            }
            if(courseId.equals(prereq3)){
                map.put(prereq3,true);
            }
        }

        return !map.containsValue(false);
    }

    public boolean checkWaitingList(CourseSchedule sched){
        for (Registration registration: registrations
        ) {
            if(registration.getCourseSchedule().getCrn().equals(sched.getCrn())){
                if(registration.getStatus().equals("Waiting List"))
                    return true;
            }
        }
        return false;
    }

    public boolean checkRegistered(CourseSchedule sched){
        for (Registration registration: registrations
        ) {
            if(registration.getCourseSchedule().getCrn().equals(sched.getCrn())){
                if(registration.getStatus().equals("Registered"))
                    return true;
            }
        }
        return false;
    }

    public int getTotalHours(String semester,int year){
        int counter = 0;
        for (Registration registration: registrations
        ) {
            if(registration.getStatus().equals("Registered")) {
                CourseSchedule courseSched = registration.getCourseSchedule();
                if (courseSched.getSemester().toLowerCase().equals(semester.toLowerCase()) && courseSched.getYear() == year)
                    counter += registration.getCourseSchedule().getCourseObj().getCreditHours();
            }
        }
        return counter;
    }

    public Boolean reachedMaxCH(String semester, int year,CourseSchedule courseSched){
        if ((getTotalHours(semester,year)+courseSched.getCourseObj().getCreditHours())>21)
            return true;
        else
            return false;
    }

    public void dropCourse(String crn) {

        for(int i=0;i<registrations.size();i++){
            if(registrations.get(i).getCourseSchedule().getCrn().equals(crn)){
                registrations.remove(i);
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.enrolledYear);
        dest.writeInt(this.currSemester);
        dest.writeDouble(this.cummGPA);
        dest.writeString(this.status);
        dest.writeString(this.major);
        dest.writeString(this.minor);
        dest.writeList(this.registrations);
        dest.writeInt(this.getQUID());
        dest.writeString(this.getFirstName());
        dest.writeString(this.getLastName());
        dest.writeLong(this.getDob() != null ? this.getDob().getTime() : -1);
        dest.writeString(this.getGender());
        dest.writeString(this.getEmail());
        dest.writeString(this.getPhoneNo());
        dest.writeString(this.getCollege());
    }

    protected Student(Parcel in) {
        this.enrolledYear = in.readInt();
        this.currSemester = in.readInt();
        this.cummGPA = in.readDouble();
        this.status = in.readString();
        this.major = in.readString();
        this.minor = in.readString();
        this.registrations = new ArrayList<Registration>();
        in.readList(this.registrations, Registration.class.getClassLoader());
        this.setQUID(in.readInt());
        this.setFirstName(in.readString());
        this.setLastName(in.readString());

        long tmpDob = in.readLong();
        this.setDob( new Date(tmpDob));
        this.setGender(in.readString());
        this.setEmail(in.readString());
        this.setPhoneNo(in.readString());
        this.setCollege(in.readString());


    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
