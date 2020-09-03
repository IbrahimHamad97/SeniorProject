package com.cmps312.courseregistration.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.controller.Interactions;
import com.cmps312.courseregistration.controller.RegisteredCourseAdapter;
import com.cmps312.courseregistration.db.DatabaseSystem;
import com.cmps312.courseregistration.model.Course;
import com.cmps312.courseregistration.model.CourseSchedule;
import com.cmps312.courseregistration.model.Registration;
import com.cmps312.courseregistration.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class WaitingListActivity extends AppCompatActivity {

    private TextView tvHeader;
    private RecyclerView rvCourseList;
    private RegisteredCourseAdapter adapter;
    private Interactions interactions;
    private Student currentUser;
    private ArrayList<CourseSchedule> courseList;
    private ArrayList<Course> classList;
    private FirebaseFirestore db;
    private String semester;
    private DatabaseSystem dbs;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_management);

        setTitle("Waiting List");

        //data setup
        interactions = new Interactions(this);
        db = FirebaseFirestore.getInstance();
        dbs = new DatabaseSystem(this);

        currentUser = getIntent().getParcelableExtra(Interactions.STUDENTKEY);
        semester = getIntent().getStringExtra(Interactions.SEMESTERKEY);
        year = getIntent().getIntExtra(Interactions.YEARKEY, 2000);
        classList = new ArrayList<>();
        courseList = new ArrayList<>();

        //data setup#Populate classList
        db.collection("Courses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Course course = document.toObject(Course.class);

                                course.setId(document.getId());
                                classList.add(course);
                            }
                        } else {
                            Log.d("C1Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
        //data setup#Populate courseList
        for (Registration reg : currentUser.getRegistrations()) {
            if (reg.getStatus().toLowerCase().equals("waiting list"))
                courseList.add(reg.getCourseSchedule());
        }

        //views setup
        tvHeader = findViewById(R.id.tv_semester);
        rvCourseList = findViewById(R.id.rv_course_list);
        rvCourseList.setHasFixedSize(true);
        rvCourseList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RegisteredCourseAdapter(this, courseList, new RegisteredCourseAdapter.RegCourseInteractions() {
            @Override
            public void onClickToggleActions(View view, View actions) {
                interactions.toggleListItemActions(view, actions);
            }

            @Override
            public void onClickInfo(View view, CourseSchedule course) {
                interactions.showCourseInfo(view, course, classList);
            }

            @Override
            public void onAddCourse(CourseSchedule course) {
                if (!course.isFull())
                    addCourse(course);
                else
                    Toast.makeText(getApplicationContext(), "Cannot register; Course is full",
                            Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDropCourse(CourseSchedule course) {
                dropCourse(course);
            }
        }, true);

        tvHeader.setText(getString(R.string.semester_ph, semester, year));
        rvCourseList.setAdapter(adapter);

    }

    private void addCourse(CourseSchedule course) {
        for (Registration reg : currentUser.getRegistrations()) {
            if (reg.getCourseSchedule().getCrn().equals(course.getCrn()))
                reg.setStatus("Registered");
        }
        dbs.updateUser(currentUser);
        course.setActual(course.getActual()+1);
        dbs.updateCourseActual(course, adapter);
        Toast.makeText(this,
                String.format(Locale.US, "Course CRN [%s] registered", course.getCrn()),
                Toast.LENGTH_LONG).show();
        courseList.remove(course);
        adapter.notifyDataSetChanged();
    }

    private void dropCourse(final CourseSchedule course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Drop Course");
        builder.setMessage("Are you sure you want to drop from the course's waiting List?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean labRequirement = course.getCourseObj().getLab();
                        ArrayList<String> removedCourse = new ArrayList<>();
                        if (labRequirement) {
                            for (Registration r : currentUser.getRegistrations()) {
                                CourseSchedule currCourse = r.getCourseSchedule();
                                if (currCourse.getCourseObj().getCourseName().equals(course.getCourseObj().getCourseName())) {
                                    removedCourse.add(r.getCourseSchedule().getCrn());
                                    courseList.remove(r.getCourseSchedule());
                                }
                            }

                        } else {
                            removedCourse.add(course.getCrn());
                            courseList.remove(course);
                        }

                        for (String rCourse : removedCourse) {
                            currentUser.dropCourse(rCourse);
                        }

                        dbs.updateUser(currentUser);
                        adapter.notifyDataSetChanged();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
