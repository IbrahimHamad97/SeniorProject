package com.cmps312.courseregistration.db;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.controller.CourseAdapter;
import com.cmps312.courseregistration.controller.Interactions;
import com.cmps312.courseregistration.model.Course;
import com.cmps312.courseregistration.model.CourseSchedule;
import com.cmps312.courseregistration.model.Hold;
import com.cmps312.courseregistration.model.Registration;
import com.cmps312.courseregistration.model.Student;
import com.cmps312.courseregistration.view.CourseRegistrationActivity;
import com.cmps312.courseregistration.view.MainActivity;
import com.cmps312.courseregistration.view.StudentProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

public class DatabaseSystem {
    private static final String TAG = DatabaseSystem.class.getSimpleName();
    private FirebaseFirestore db;
    private Context context;
    private DocumentReference dbUser;
    private FirebaseUser user;

    public DatabaseSystem(Context context) {
        this.context = context;
        FirebaseApp.initializeApp(context);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbUser = db.collection("Users").document(user.getUid());

    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void sendHolds(final String uid, final Intent intent) {
        //For response testing purposes
        long startTimer = (new java.util.Date()).getTime();
        intent.putExtra(Interactions.TESTVALKEY, startTimer);

        final ArrayList<Hold> holds = new ArrayList<>();
        db.collection("holds")
                .whereEqualTo("student", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Timestamp ts = document.getTimestamp("receivedDate");
                                java.util.Date d = ts.toDate();
                                Hold currentHold = new Hold();
                                currentHold.setReason(document.getString("reason"));
                                currentHold.setOriginator(document.getString("originator"));
                                currentHold.setReceivedDate(d);

                                Log.i(TAG, "onComplete: " + d.toString());

                                holds.add(currentHold);
                            }
                            intent.putParcelableArrayListExtra(Interactions.HOLDSKEY, holds);
                            context.startActivity(intent);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void sendCourses(final String uid, final ArrayList<CourseSchedule> courseList,
                            final Student currentUser, final int year, final String semester,
                            final Intent intent) {
        final long startTimer = (new java.util.Date()).getTime();

        courseList.clear();
        db.collection("courseSchedules")
                .whereEqualTo("gender", currentUser.getGender())
                .whereEqualTo("year", year)
                .whereEqualTo("semester", semester.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                CourseSchedule course = document.toObject(CourseSchedule.class);
                                courseList.add(course);


                            }
                            DocumentReference dbUser = db.collection("Users").document(uid);
                            dbUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Student currentUser = documentSnapshot.toObject(Student.class);
                                    intent.putParcelableArrayListExtra("courseList", courseList);
                                    intent.putExtra("year", year);
                                    intent.putExtra("semester", semester);
                                    intent.putExtra(Interactions.STUDENTKEY, currentUser);
                                    intent.putExtra(Interactions.TESTVALKEY, startTimer);
                                    context.startActivity(intent);

                                }
                            });


                        } else {
                            Log.d("FAIL", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void sendStudent(final String uid, final Intent intent) {
        //For testing purposes
        long startTimer = (new java.util.Date()).getTime();
        intent.putExtra(Interactions.TESTVALKEY, startTimer);

        DocumentReference dbUser = db.collection("Users").document(uid);
        dbUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Student currentUser = documentSnapshot.toObject(Student.class);
                intent.putExtra(Interactions.STUDENTKEY, currentUser);

                context.startActivity(intent);

            }
        });
    }

    public void dropCourse(final CourseSchedule course, final RecyclerView.Adapter adapter) {

        String crn = course.getCrn();

        db.collection("courseSchedules")
                .whereEqualTo("crn", crn)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("courseSchedules")
                                        .document(document.getId())
                                        .update("actual",
                                                FieldValue.increment(-1));

                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void dropCourses(final ArrayList<String> crns, final RecyclerView.Adapter adapter) {

        db.collection("courseSchedules")
                .whereIn("crn", crns)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("courseSchedules")
                                        .document(document.getId())
                                        .update("actual", FieldValue.increment(-1));

                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void addCourse(final CourseSchedule cs, final RecyclerView.Adapter adapter) {
        db.collection("courseSchedules")
                .whereEqualTo("crn", cs.getCrn())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("courseSchedules")
                                        .document(document.getId())
                                        .update("actual", FieldValue.increment(1));
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void updateCourseActual(final CourseSchedule cs, final RecyclerView.Adapter adapter) {
        db.collection("courseSchedules")
                .whereEqualTo("crn", cs.getCrn())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("courseSchedules")
                                        .document(document.getId())
                                        .update("actual", cs.getActual());
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void updateUser(final Student currentUser) {
                dbUser = db.collection("Users").document(user.getUid());
        dbUser.set(currentUser);
    }

}
