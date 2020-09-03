package com.cmps312.courseregistration.view;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.controller.Interactions;
import com.cmps312.courseregistration.model.Student;

import java.util.Date;

public class StudentProfileActivity extends AppCompatActivity {

    private TextView tvName, tvQUID, tvEmail, tvMajor, tvMinor, tvGPA, tvRegistered;
    private ImageView ivCol;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        setTitle("My Profile");

        student = getIntent().getParcelableExtra(Interactions.STUDENTKEY);
        long startTimer = getIntent().getLongExtra(Interactions.TESTVALKEY, 0);

        //Associate textviews
        tvName = findViewById(R.id.tv_stdnt_name);
        tvQUID = findViewById(R.id.tv_stdnt_id);
        tvEmail = findViewById(R.id.tv_stdnt_email);
        tvMajor = findViewById(R.id.tv_stdnt_major);
        tvMinor = findViewById(R.id.tv_stdnt_minor);
        tvGPA = findViewById(R.id.tv_stdnt_gpa);
        tvRegistered = findViewById(R.id.tv_stdnt_reg_year);
        ivCol = findViewById(R.id.iv_stdnt_col);

        //Append data to text views
        String name = student.getFirstName()+" "+student.getLastName();
        tvName.setText(name);
        tvQUID.setText(student.getQUID()+"");
        tvEmail.setText(student.getEmail());
        tvMajor.setText(student.getMajor());
        tvMinor.setText(student.getMinor());
        tvGPA.setText(student.getCummGPA()+"");
        tvRegistered.setText(student.getEnrolledYear()+"");
        setColImage();

        long diff = (new Date()).getTime() - startTimer;
        Log.i("DIFF", "TIMER DIFFERENCE PROFILE ACTIVITY =  " + diff);

    }

    //set imageview to icon associated with college
    private void setColImage() {
        String college = student.getCollege();
        int resource = -1;

        if (college.contains("Arts and Sciences"))
            resource = R.drawable.col_a_s;
        else if (college.contains("Business and Economics"))
            resource = R.drawable.col_bus;
        else if (college.contains("Education"))
            resource = R.drawable.col_edu;
        else if (college.contains("Engineering"))
            resource = R.drawable.col_eng;
        else if (college.contains("Health Sciences"))
            resource = R.drawable.col_hea;
        else if (college.contains("Law"))
            resource = R.drawable.col_law;
        else if (college.contains("Medicine"))
            resource = R.drawable.col_med;
        else if (college.contains("Pharmacy"))
            resource = R.drawable.col_pha;
        else if (college.contains("Sharia and Islamic Studies"))
            resource = R.drawable.col_isl;

        if (resource != -1)
            ivCol.setImageDrawable(getDrawable(resource));
    }
}
