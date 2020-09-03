package com.cmps312.courseregistration.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.controller.Interactions;
import com.cmps312.courseregistration.db.DatabaseSystem;
import com.cmps312.courseregistration.model.CourseSchedule;
import com.cmps312.courseregistration.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private Student currentUser;
    private ArrayList<CourseSchedule> courseList;
    private Spinner semesterSpinner;
    private String semester;
    private int year;
    private FirebaseUser fbuser;
    private Button btnCourseReg, btnCourseMng, btnWaitingList, btnSched, btnProf, btnHolds, btnBan;
    private DatabaseSystem dbs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbs = new DatabaseSystem(this);

        //ASSOC buttons
        btnCourseReg = findViewById(R.id.btn_course_reg);
        btnCourseMng = findViewById(R.id.btn_course_manage);
        btnWaitingList = findViewById(R.id.btn_waiting_list);
        btnSched = findViewById(R.id.btn_my_sched);
        btnProf = findViewById(R.id.btn_my_info);
        btnHolds = findViewById(R.id.btn_view_holds);
        btnBan = findViewById(R.id.btn_mybanner);
        // SET effect listener
        Interactions.ButtonEffectListener bfl = new Interactions.ButtonEffectListener();
        btnCourseReg.setOnTouchListener(bfl);
        btnCourseMng.setOnTouchListener(bfl);
        btnWaitingList.setOnTouchListener(bfl);
        btnSched.setOnTouchListener(bfl);
        btnProf.setOnTouchListener(bfl);
        btnHolds.setOnTouchListener(bfl);
        btnBan.setOnTouchListener(bfl);

        //ASSOC data
        semesterSpinner = findViewById(R.id.semester_spinner2);
        db = FirebaseFirestore.getInstance();
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        courseList = new ArrayList<>();
        currentUser = getIntent().getExtras().getParcelable("user");
        //PERF TEST LOGIN
        long startTimer = getIntent().getLongExtra(Interactions.TESTVALKEY, 0);
        long diff = (new Date()).getTime() - startTimer;
        Log.i("DIFF", "TIMER DIFFERENCE MAIN ACTIVITY =  " + diff);

        setTitle("Hi, " + currentUser.getFirstName() + " " + currentUser.getLastName());

        ArrayAdapter<CharSequence> semesterAdapter = ArrayAdapter.createFromResource(this,
                R.array.semester_list, android.R.layout.simple_spinner_item);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        semesterSpinner.setAdapter(semesterAdapter);
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {
                String[] seperated = parent.getItemAtPosition(position).toString().split(" ");
                semester = seperated[0];
                year = Integer.parseInt(seperated[1]);
                Log.d("test166", "onItemSelected: " + semester);
                Log.d("test166", "onItemSelected: " + year);

            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


    }

    public void openCourseRegistrationActivity(View view) {
        Intent intent = new Intent(this, CourseRegistrationActivity.class);
        dbs.sendCourses(fbuser.getUid(), courseList, currentUser, year, semester, intent);
    }

    public void openCourseManagementActivity(View view) {
        Intent intent = new Intent(this, CourseManagementActivity.class);
        intent.putExtra(Interactions.SEMESTERKEY, semester);
        intent.putExtra(Interactions.YEARKEY, year);
        dbs.sendStudent(fbuser.getUid(), intent);
    }

    public void openWaitingListActivity(View view) {
        Intent intent = new Intent(this, WaitingListActivity.class);
        intent.putExtra(Interactions.SEMESTERKEY, semester);
        intent.putExtra(Interactions.YEARKEY, year);
        dbs.sendStudent(fbuser.getUid(), intent);
    }

    public void openStudentProfileActivity(View view) {
        Intent intent = new Intent(this, StudentProfileActivity.class);
        dbs.sendStudent(fbuser.getUid(), intent);
    }

    public void openHoldsActivity(View view) {
        String quid = currentUser.getQUID() + "";
        Intent intent = new Intent(this, HoldsActivity.class);
        dbs.sendHolds(quid, intent);
    }

    public void openMyScheduleActivity(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        dbs.sendStudent(fbuser.getUid(), intent);
    }


    public void sendToMyBanner(View view) {
        String url = this.getString(R.string.mybanner_link);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }
}
