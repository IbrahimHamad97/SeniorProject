package com.cmps312.courseregistration.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cmps312.courseregistration.R;
import com.cmps312.courseregistration.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEt;
    private EditText passwordEt;
    private TextView errorTv;
    private TextView passwordRecTv;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        db=FirebaseFirestore.getInstance();
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        errorTv = findViewById(R.id.errorTv);
        passwordRecTv = findViewById(R.id.passwordRecTv);
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View view) {

        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        if(email.equals("") || password.equals("")){
            errorTv.setText("Please enter in your login details");
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            DocumentReference dbUser = db.collection("Users").document(user.getUid());
                            dbUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Student currentUser = documentSnapshot.toObject(Student.class);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("user",currentUser);
                                    startActivity(intent);

                                }
                            });

                        } else {
                            errorTv.setText("Login Error. Email or Password was incorrect.");

                        }

                    }
                });
    }

    public void openMyQU(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(this.getString(R.string.myid_qu_link)));
        startActivity(intent);
    }
}

