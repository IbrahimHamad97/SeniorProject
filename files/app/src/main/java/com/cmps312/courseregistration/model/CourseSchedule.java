package com.cmps312.courseregistration.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CourseSchedule implements Parcelable {
    private String crn;
    private String course;
    private String day;
    private String gender;
    private int capacity;
    private int actual;
    private String section;
    private String location;
    private ArrayList<String> instructor = new ArrayList<>();
    private ArrayList<SchedGroup> schedGroups = new ArrayList<>();
    private int startTime;
    private int endTime;
    private String semester;
    private int year;
    private Course courseObj;

    public CourseSchedule() {
    }

    public CourseSchedule(String crn, String course, String day, String gender, int capacity, int actual, String section, String location, ArrayList<String> instructors, int startTime, int endTime, String semester, int year) {
        this.crn = crn;
        this.course = course;
        this.day = day;
        this.gender = gender;
        this.capacity = capacity;
        this.actual = actual;
        this.section = section;
        this.location = location;
        this.instructor = instructors;
        this.startTime = startTime;
        this.endTime = endTime;
        this.semester = semester;
        this.year = year;
    }

    public Course getCourseObj() {
        return courseObj;
    }

    public void setCourseObj(Course courseObj) {
        this.courseObj = courseObj;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getInstructor() {
        return instructor;
    }

    public void setInstructor(ArrayList<String> instructors) {
        this.instructor = instructors;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public ArrayList<SchedGroup> getSchedGroups() {
        return schedGroups;
    }

    public void setSchedGroups(ArrayList<SchedGroup> schedGroups) {
        this.schedGroups = schedGroups;
    }

    public boolean isFull() {
        return actual >= capacity;
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
        dest.writeString(this.crn);
        dest.writeString(this.course);
        dest.writeString(this.day);
        dest.writeString(this.gender);
        dest.writeInt(this.capacity);
        dest.writeInt(this.actual);
        dest.writeString(this.section);
        dest.writeString(this.location);
        dest.writeStringList(this.instructor);
        dest.writeTypedList(this.schedGroups);
        dest.writeInt(this.startTime);
        dest.writeInt(this.endTime);
        dest.writeString(this.semester);
        dest.writeInt(this.year);
        dest.writeParcelable(this.courseObj, flags);
    }

    protected CourseSchedule(Parcel in) {
        this.crn = in.readString();
        this.course = in.readString();
        this.day = in.readString();
        this.gender = in.readString();
        this.capacity = in.readInt();
        this.actual = in.readInt();
        this.section = in.readString();
        this.location = in.readString();
        this.instructor = in.createStringArrayList();
        this.schedGroups = in.createTypedArrayList(SchedGroup.CREATOR);
        this.startTime = in.readInt();
        this.endTime = in.readInt();
        this.semester = in.readString();
        this.year = in.readInt();
        this.courseObj = in.readParcelable(Course.class.getClassLoader());
    }

    public static final Creator<CourseSchedule> CREATOR = new Creator<CourseSchedule>() {
        @Override
        public CourseSchedule createFromParcel(Parcel source) {
            return new CourseSchedule(source);
        }

        @Override
        public CourseSchedule[] newArray(int size) {
            return new CourseSchedule[size];
        }
    };

    //Return Schedule string (e.g. U T R: 8:00 - 8:50
    public String getScheduleString() {
        StringBuilder sb = new StringBuilder();
        for (SchedGroup sg : schedGroups) {
            StringBuilder sbd = new StringBuilder();
            for (String day : sg.getDays()) {
                sbd.append(day).append(" ");
            }
            sb.append(String.format(java.util.Locale.US, "%s: %s-%s\n",
                    sbd.toString().trim(), formatTime(sg.getStartTime()),
                    formatTime(sg.getEndTime())));
        }
        return sb.toString();
    }

    private String formatTime(int time) {

        String timeStamp;
        String timeString = time + "";

        if (time < 969) {
            String hour = timeString.substring(0, 1);
            String minute = timeString.substring(1, 3);
            timeStamp = "0" + hour + ":" + minute;
        } else {
            String hour = timeString.substring(0, 2);
            String minute = timeString.substring(2, 4);
            timeStamp = hour + ":" + minute;
        }

        return timeStamp;
    }
}
