package com.cmps312.courseregistration.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.controller.CourseAdapter;
import com.cmps312.courseregistration.controller.Interactions;
import com.cmps312.courseregistration.db.DatabaseSystem;
import com.cmps312.courseregistration.model.Course;
import com.cmps312.courseregistration.model.CourseSchedule;
import com.cmps312.courseregistration.model.OverrideRequest;
import com.cmps312.courseregistration.model.Registration;
import com.cmps312.courseregistration.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;



public class CourseRegistrationActivity extends AppCompatActivity {

    String TAG = "FIRESTORE_ERROR";
    private RecyclerView rvCourseList;
    private CourseAdapter adapter;
    private Interactions interactions;
    private TextView tvTotalCredit;
    private Button confirmRegBtn;
    private ArrayList<CourseSchedule> courseList;
    private ArrayList<CourseSchedule> filteredCourseList;
    private ArrayList<Course> classList;
    private ArrayList<String> subjects;
    private ArrayList<String> courses;
    private ArrayList<Integer> choice;
    private DatabaseSystem dbs;

    private Spinner subjectSpinner;
    private Spinner courseSpinner;
    private FirebaseFirestore db;
    private Student currentUser;
    private int year;
    private String semester;
    private DocumentReference dbUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_registration);
        dbs = new DatabaseSystem(this);
        db = dbs.getDb();

        subjectSpinner = findViewById(R.id.subject_spinner);
        courseSpinner = findViewById(R.id.course_spinner);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        confirmRegBtn = findViewById(R.id.confirm_btn);
        tvTotalCredit = findViewById(R.id.tv_total_credit_hours);
        interactions = new Interactions(this);
        rvCourseList = findViewById(R.id.rv_course_list);

        filteredCourseList = new ArrayList<>();
        rvCourseList.setHasFixedSize(true);
        rvCourseList.setLayoutManager(new LinearLayoutManager(this));

        courseList = new ArrayList<>();
        classList = new ArrayList<>();
        subjects = new ArrayList<>();
        courses = new ArrayList<>();
        choice = new ArrayList<>();


        semester = getIntent().getStringExtra("semester");
        year = getIntent().getIntExtra("year", 2000);
        currentUser = getIntent().getExtras().getParcelable(Interactions.STUDENTKEY);
        courseList = getIntent().getParcelableArrayListExtra("courseList");
        tvTotalCredit.setText(getResources().getString(R.string.total_credit_hours_ph, currentUser.getTotalHours(semester, year)));

        //Populate classList
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



        ArrayList<String> tempSubj = new ArrayList<>();
        for (CourseSchedule sched : courseList) {
            tempSubj.add(sched.getCourseObj().getSubject());
        }

        Set<String> temp = new HashSet<String>(tempSubj);
        subjects.addAll(temp);
        subjects.add("View Registered");
        subjects.add("View Waiting List");

        ArrayAdapter subjectsAdapter = new ArrayAdapter(CourseRegistrationActivity.this, android.R.layout.simple_spinner_item, subjects);

        subjectsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        subjectSpinner.setAdapter(subjectsAdapter);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {
                courses.clear();
                ArrayList<String> tempCourse = new ArrayList<>();
                String subject = parent.getItemAtPosition(position).toString();

                if (subject.equals("View Registered")) {
                    filteredCourseList.clear();
                    courses.clear();
                    DocumentReference currUser = db.collection("Users").document(user.getUid());
                    currUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    Student student = document.toObject(Student.class);
                                    ArrayList<Registration> studentReg = student.getRegistrations();
                                    for (Registration reg :
                                            studentReg) {
                                        if (reg.getStatus().equals("Registered"))
                                            filteredCourseList.add(reg.getCourseSchedule());
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                    courseSpinner.setVisibility(View.GONE);

                } else if (subject.equals("View Waiting List")) {
                    filteredCourseList.clear();
                    courses.clear();
                    DocumentReference currUser = db.collection("Users").document(user.getUid());
                    currUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    Student student = document.toObject(Student.class);
                                    ArrayList<Registration> studentReg = student.getRegistrations();
                                    for (Registration reg :
                                            studentReg) {
                                        if (reg.getStatus().equals("Waiting List"))
                                            filteredCourseList.add(reg.getCourseSchedule());
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                    courseSpinner.setVisibility(View.GONE);
                } else {
                    courses.clear();
                    for (CourseSchedule sched : courseList) {
                        if (sched.getCourseObj().getSubject().equals(subject))
                            tempCourse.add(sched.getCourseObj().getCourseName());
                    }
                    courseSpinner.setVisibility(View.VISIBLE);
                    Set<String> temp2 = new HashSet<String>(tempCourse);
                    courses.addAll(temp2);
                    ArrayAdapter courseAdapter = new ArrayAdapter(CourseRegistrationActivity.this, android.R.layout.simple_spinner_item, courses);
                    courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courseSpinner.setAdapter(courseAdapter);
                    courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long arg3) {
                            choice.clear();
                            filteredCourseList.clear();
                            db.collection("courseSchedules")
                                    .whereEqualTo("gender", currentUser.getGender())
                                    .whereEqualTo("courseObj.courseName", parent.getItemAtPosition(position).toString())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    CourseSchedule course = document.toObject(CourseSchedule.class);
                                                    filteredCourseList.add(course);


                                                }
                                                adapter.notifyDataSetChanged();


                                            } else {
                                                Log.d("FAIL", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });

                        }

                        public void onNothingSelected(AdapterView<?> arg0) {
                            filteredCourseList = courseList;
                        }
                    });

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        subjectSpinner.setSelection(subjectsAdapter.getPosition("View Registered"));



        adapter = new CourseAdapter(CourseRegistrationActivity.this, filteredCourseList, new CourseAdapter.CourseInteractions() {

            @Override
            public void onClickToggleActions(View view, View actions) {
                interactions.toggleListItemActions(view, actions);
            }

            @Override
            public void onClickInfo(View view, CourseSchedule course) {
                interactions.showCourseInfo(view, course, classList);
            }

            @Override
            public void onAddCourse(final ArrayList<CourseSchedule> coursesToReg) {

                final ArrayList<String> addedCrn = new ArrayList<>();
                for (CourseSchedule coursesched : coursesToReg) {
                    if (currentUser.reachedMaxCH(semester, year, coursesched)) {
                        Toast.makeText(CourseRegistrationActivity.this, "You cannot register as this will exceed your maximum allowed credit hours", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                for (final CourseSchedule tempCS : coursesToReg) {
                    db.collection("courseSchedules")
                            .whereEqualTo("crn", tempCS.getCrn())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            CourseSchedule crse = document.toObject(CourseSchedule.class);
                                            if (crse.isFull() == true) {
                                                Toast.makeText(CourseRegistrationActivity.this, "Course " + crse.getCrn() + " " + crse.getCourseObj().getCourseName() + " is full.", Toast.LENGTH_SHORT).show();
                                                filteredCourseList.set(filteredCourseList.indexOf(tempCS), crse);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                crse.setActual(crse.getActual()+1);
                                                Boolean state = currentUser.registerCourse(crse);
                                                if (state == false) {
                                                    Toast.makeText(CourseRegistrationActivity.this, "Cannot Register One of the Courses", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }
                                        }

                                        tvTotalCredit.setText(getResources().getString(R.string.total_credit_hours_ph, currentUser.getTotalHours(semester, year)));

                                        dbs.updateUser(currentUser);
                                        for (CourseSchedule tempCSin : coursesToReg) {
                                            if (!addedCrn.contains(tempCSin.getCrn())) {
                                                tempCSin.setActual(tempCSin.getActual() + 1);
                                                filteredCourseList.set(filteredCourseList.indexOf(tempCSin), tempCSin);
                                                dbs.updateCourseActual(tempCSin, adapter);

                                                addedCrn.add(tempCSin.getCrn());
                                            }


                                        }

                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                }

            }

            @Override
            public void onWaitingListCourse(CourseSchedule course) {
                currentUser.registerWaitingList(course);
                dbs.updateUser(currentUser);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDropCourse(final CourseSchedule course) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CourseRegistrationActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm Drop Course");
                builder.setMessage("Are you sure you want to drop the course? Dropping the course will also drop any linked courses.");
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
                                        }
                                    }

                                } else
                                    removedCourse.add(course.getCrn());

                                for (String rCourse : removedCourse) {
                                    currentUser.dropCourse(rCourse);
                                }
                                for (CourseSchedule cs: filteredCourseList) {
                                    if (removedCourse.contains(cs.getCrn()) && !cs.getCrn().equals(course.getCrn()))
                                            filteredCourseList.set(filteredCourseList.indexOf(cs), decCourse(cs));
                                }

                                if (removedCourse.size() > 1)
                                    dbs.dropCourses(removedCourse, adapter);
                                else
                                    dbs.dropCourse(course, adapter);

                                tvTotalCredit.setText(getResources().getString(R.string.total_credit_hours_ph, currentUser.getTotalHours(semester, year)));
                                dbs.updateUser(currentUser);
                            }
                        });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }

            @Override
            public void onOverrideCourse(final CourseSchedule course, String reason) {
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                final View popupView = inflater.inflate(R.layout.override_form, null);

                TextView overrideReason = popupView.findViewById(R.id.override_reason_tv);
                TextView advisor = popupView.findViewById(R.id.advisor_tv);
                Button confirmBtn = popupView.findViewById(R.id.cfrm_btn);
                Button cancelBtn = popupView.findViewById(R.id.cancel_btn);

                overrideReason.setText(reason);
                advisor.setText("Abeer Musa");

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(courseSpinner, Gravity.CENTER, 0, 0);

                final OverrideRequest newOverride = new OverrideRequest("Abeer Musa", reason, new Date(), currentUser.getFirstName() + " " + currentUser.getLastName(), currentUser.getQUID());

                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CourseRegistrationActivity.this, "override request has been sent", Toast.LENGTH_SHORT).show();
                        db.collection("OverrideRequests").add(newOverride);
                        popupWindow.dismiss();

                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });


            }
        }, currentUser, confirmRegBtn, choice);
        rvCourseList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.registration_menu, menu);
        return true;
    }

    private CourseSchedule decCourse(CourseSchedule cs) {
        cs.setActual(cs.getActual() - 1);
        return cs;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.displayStudyPlan:
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.display_studyplan, null);

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(courseSpinner, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
                return true;
            case R.id.returnMain:
                Intent intent = new Intent(CourseRegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


