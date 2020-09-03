package com.cmps312.courseregistration.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.controller.Interactions;
import com.cmps312.courseregistration.controller.ScheduleTable;
import com.cmps312.courseregistration.model.Registration;
import com.cmps312.courseregistration.model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

public class ScheduleActivity extends AppCompatActivity {

    private ScheduleTable st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        setTitle("My Schedule");

        Student student = getIntent().getParcelableExtra(Interactions.STUDENTKEY);
        //get Registrations from student object, let scheduleTable format it
        ArrayList<Registration> regs = new ArrayList<>();
        for (Registration reg: student.getRegistrations()) {
            if (reg.getStatus().equals("Registered"))
                regs.add(reg);
        }
        regs.sort(new Comparator<Registration>() {
            @Override
            public int compare(Registration o1, Registration o2) {
                return o1.getCourseSchedule().getStartTime() - o2.getCourseSchedule().getStartTime();
            }
        });
        st = new ScheduleTable(regs);

        populateRows();

        long startTimer = getIntent().getLongExtra(Interactions.TESTVALKEY, 0);
        long diff = (new Date()).getTime() - startTimer;
        Log.i("DIFF", "TIMER DIFFERENCE =  " + diff);
    }

    public void populateRows() {
        TableLayout t1 = (TableLayout) findViewById(R.id.tl_schedule);

        for (int c = 0; c < st.size(); c++) {
            TableRow tr = new TableRow(this);
            tr.setId(1 + c);
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            TextView[] tvSched = new TextView[6];

            tvSched[0] = formatTv(R.color.colorPrimaryDark, st.getRow(c).getTimegroup());
            tvSched[0].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(tvSched[0], new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT
            ));

            for (int i = 1; i < tvSched.length; i++) {
                int bg;
                if (i==2 || i==4)
                    bg = R.color.colorLightBg;
                else
                    bg = android.R.color.white;
                tvSched[i] = formatTv(bg, st.getRow(c).getDay(i-1));
                tvSched[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tr.addView(tvSched[i], new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT
                ));
            }

            tr.setPaddingRelative(0, 10, 0, 10);
            t1.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        }


    }

    private TextView formatTv(int bgColor, String text) {
        TextView tv = new TextView(this);
        tv.setBackgroundColor(getResources().getColor(bgColor));
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(5, 5, 5, 5);
        tv.setText(text);
        return tv;
    }


}
