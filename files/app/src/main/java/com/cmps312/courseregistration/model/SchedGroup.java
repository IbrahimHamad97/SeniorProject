package com.cmps312.courseregistration.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class SchedGroup implements Parcelable {
    private ArrayList<String> days = new ArrayList<>();
    private int startTime;
    private int endTime;

    public SchedGroup(){

    }

    public SchedGroup(ArrayList<String> days, int startTime, int endTime) {
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
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

    @NonNull
    @Override
    public String toString() {
        String stringDays = "";
        for(String day: days){
            stringDays=stringDays+" "+day;
        }
        int startHours = startTime/ 100;
        int startMinutes = (startTime - startHours * 100) % 60;
        int endHours = endTime/ 100;
        int endMinutes = (endTime - endHours * 100) % 60;
        return stringDays+": "+String.format("%02d:%02d", startHours,startMinutes)+"-"+String.format("%02d:%02d", endHours,endMinutes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.days);
        dest.writeInt(this.startTime);
        dest.writeInt(this.endTime);
    }

    protected SchedGroup(Parcel in) {
        this.days = in.createStringArrayList();
        this.startTime = in.readInt();
        this.endTime = in.readInt();
    }

    public static final Creator<SchedGroup> CREATOR = new Creator<SchedGroup>() {
        @Override
        public SchedGroup createFromParcel(Parcel source) {
            return new SchedGroup(source);
        }

        @Override
        public SchedGroup[] newArray(int size) {
            return new SchedGroup[size];
        }
    };
}
