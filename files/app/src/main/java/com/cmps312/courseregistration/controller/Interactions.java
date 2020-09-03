package com.cmps312.courseregistration.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.model.Course;
import com.cmps312.courseregistration.model.CourseSchedule;

import java.util.ArrayList;


//Class meant to unify activity interactions and simplify activity code
public class Interactions {

    public static final String COURSEKEY = "course_key";
    public static final String HOLDSKEY = "holds_key";
    public static final String STUDENTKEY = "students_key";
    public static final String SEMESTERKEY = "semester";
    public static final String YEARKEY = "year";
    public static final String TESTVALKEY = "test_key";
    private Context context;
    private View activeItem, activeActions;

    public Interactions(Context context) {
        this.context = context;
    }

    //Toggle visibility of actions of an item in recycler list
    public void toggleListItemActions(View view, View actions) {
        if (activeItem != null && activeActions != null && activeItem != view) {
            toggleItemActions(activeItem, activeActions);
        }

        toggleItemActions(view, actions);
    }

    private void toggleItemActions(View view, View actions) {
        if (actions.getVisibility() == View.GONE) {
            actions.setVisibility(View.VISIBLE);
            activeItem = view;
            activeActions = actions;
        } else {
            actions.setVisibility(View.GONE);
            activeItem = null;
            activeActions = null;
        }
    }


    //Calculate total credit hours of courses to be registered and update textview
    public void CalcTotalHours(ArrayList<CourseSchedule> courseList, TextView tvTotalCredit) {
        int chSum = 0;
        for (CourseSchedule course: courseList) {
            chSum += course.getCourseObj().getCreditHours();
        }

        tvTotalCredit.setText(context.getString(R.string.total_credit_hours_ph, chSum));
    }

    //Listener to apply click highlight effect to buttons
    public static class ButtonEffectListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.getBackground().setColorFilter(0x5503A9F4,PorterDuff.Mode.SRC_ATOP);
                    v.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.getBackground().clearColorFilter();
                    v.invalidate();
                    break;
                }
            }
            return false;
        }
    }

    public void showCourseInfo(View view, CourseSchedule course, ArrayList<Course> classList) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_course_info, null);

        TextView crnTv = popupView.findViewById(R.id.tv_crn);
        TextView titleTv = popupView.findViewById(R.id.tv_courseTitle);
        TextView codeTv = popupView.findViewById(R.id.tv_code);
        TextView instructorTv = popupView.findViewById(R.id.tv_instructor);
        TextView subjectTv = popupView.findViewById(R.id.tv_subject);
        TextView locationTv = popupView.findViewById(R.id.tv_location);
        TextView creditHrsTv = popupView.findViewById(R.id.tv_credithrs);
        TextView remainingTv = popupView.findViewById(R.id.tv_actualseats);
        TextView totalTv = popupView.findViewById(R.id.tv_totalseats);
        TextView prereq1Tv = popupView.findViewById(R.id.tv_prereq1);
        TextView prereq2Tv = popupView.findViewById(R.id.tv_prereq2);
        TextView prereq3Tv = popupView.findViewById(R.id.tv_prereq3);
        TextView dateTv = popupView.findViewById(R.id.tv_time);
        TextView schedTv = popupView.findViewById(R.id.tv_courseSched);

        schedTv.setText(course.getScheduleString());
        dateTv.setText(course.getSchedGroups().get(0).toString());
        crnTv.setText(course.getCrn());
        titleTv.setText(course.getCourseObj().getCourseName());
        codeTv.setText(course.getCourseObj().getCode() + "");
        instructorTv.setText(course.getInstructor().get(0));
        subjectTv.setText(course.getCourseObj().getSubject());
        locationTv.setText(course.getLocation());
        creditHrsTv.setText(course.getCourseObj().getCreditHours() + "");
        remainingTv.setText(course.getActual() + "");
        totalTv.setText(course.getCapacity() + "");
        for (Course t :
                classList) {
            if (t.getId().equals(course.getCourseObj().getPreRequisiteCourse1()))
                prereq1Tv.setText(t.getCourseName());
            if (t.getId().equals(course.getCourseObj().getPreRequisiteCourse2()))
                prereq2Tv.setText(t.getCourseName());
            if (t.getId().equals(course.getCourseObj().getPreRequisiteCourse3()))
                prereq3Tv.setText(t.getCourseName());

        }
        if (prereq1Tv.getText().equals("PH"))
            prereq1Tv.setText("None");
        if (prereq2Tv.getText().equals("PH"))
            prereq2Tv.setText("None");
        if (prereq3Tv.getText().equals("PH"))
            prereq3Tv.setText("None");


        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }


}
