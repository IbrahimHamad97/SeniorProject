package com.cmps312.courseregistration.controller;

import android.util.Log;

import com.cmps312.courseregistration.model.CourseSchedule;
import com.cmps312.courseregistration.model.Registration;
import com.cmps312.courseregistration.model.SchedGroup;

import java.util.ArrayList;
import java.util.Locale;

public class ScheduleTable {
    private ArrayList<schedTableRow> rows;

    public static class schedTableRow {
        String timegroup;
        // 0: Sunday, 1: Monday, 2: Tuesday, 3: Wednesday, 4:Thursday
        String[] days = new String[5];

        public schedTableRow() {
            this.timegroup = "";

            this.days[0] = "";
            this.days[1] = "";
            this.days[2] = "";
            this.days[3] = "";
            this.days[4] = "";
        }

        public schedTableRow(String timegroup) {
            this.timegroup = timegroup;

            this.days[0] = "";
            this.days[1] = "";
            this.days[2] = "";
            this.days[3] = "";
            this.days[4] = "";
        }

        public String getTimegroup() {
            return timegroup;
        }

        public void setTimegroup(String timegroup) {
            this.timegroup = timegroup;
        }

        public String getDay(int position) {
            return days[position];
        }
        
        public void setDay(int position, String crs) {
            days[position] = crs;
        }
    }


    public int size() {
        return rows.size();
    }

    public schedTableRow getRow(int position) {
        return rows.get(position);
    }

    public void setRows(ArrayList<schedTableRow> rows) {
        this.rows = rows;
    }

    public ScheduleTable(ArrayList<Registration> regs) {
        rows = new ArrayList<>();

        for (Registration reg : regs) {
            makeRow(reg);
        }

    }

    //Create a timegroup row or add to a row if course schedule is part of the same timegroup
    private void makeRow(Registration reg) {

        for (SchedGroup sg : reg.getCourseSchedule().getSchedGroups()) {
            String timeRange = String.format(Locale.US, "%s\n-\n%s", sg.getStartTime(), sg.getEndTime());
            boolean timegroupFound = false;
            for (schedTableRow row : rows) {
                if (timeRange.equals(row.getTimegroup())) {
                    addToDays(row, reg, sg);
                    timegroupFound = true;
                }
            }
            if (!timegroupFound) {
                addToDays(null, reg, sg);
            }

        }
    }

    //Append course label and location to a slot for each day
    private void addToDays(schedTableRow row, Registration reg, SchedGroup sg) {
        String formattedLocation = String.format(Locale.US,"%s\n%s",
                reg.getCourseSchedule().getLocation().split(" ")[0],
                reg.getCourseSchedule().getLocation().split(" ")[1]);

        String formatted = String.format(Locale.US, "%s\n-\n%s",
                reg.getCourseSchedule().getCourseObj().getCourseCodeLabel(), formattedLocation);

        if (row == null) {
            String timeRange = String.format(Locale.US, "%s\n-\n%s",
                    formatTime(sg.getStartTime()), formatTime(sg.getEndTime()));
            rows.add(new schedTableRow(timeRange));
            row = rows.get(rows.size()-1);
        }

        if (sg.getDays().contains("U"))
            row.setDay(0, formatted);
        if (sg.getDays().contains("M"))
            row.setDay(1, formatted);
        if (sg.getDays().contains("T"))
            row.setDay(2, formatted);
        if (sg.getDays().contains("W"))
            row.setDay(3, formatted);
        if (sg.getDays().contains("R"))
            row.setDay(4, formatted);

    }

    //Format integers to timestamps
    private String formatTime(int time) {

        String timeStamp;
        String timeOfDay = "AM";

        if (time >= 1200 && time < 2400) {
            timeOfDay = "PM";
            if (time >= 1300)
                time -= 1200;
        }

        String timeString = time + "";

        if (time < 969) {
           String hour = timeString.substring(0,1);
           String minute = timeString.substring(1,3);
           timeStamp = "0" + hour + ":" + minute;
        } else {
            String hour = timeString.substring(0,2);
            String minute = timeString.substring(2,4);
            timeStamp = hour + ":" + minute;
        }

        return String.format(Locale.US,"%s\n%s",timeStamp, timeOfDay);
    }


}
