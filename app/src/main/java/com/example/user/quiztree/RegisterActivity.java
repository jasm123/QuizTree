package com.example.user.quiztree;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
   private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private FirebaseAuth auth;

    @Bind(R.id.name) EditText _nameText;
    @Bind(R.id.grade) EditText _gradeText;
    @Bind(R.id.email) EditText inputEmail;
    @Bind(R.id.password) EditText inputPassword;
    @Bind(R.id.sign_up_button) Button btnSignUp;
    @Bind(R.id.sign_in_button) Button btnSignIn;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
       mDatabase = FirebaseDatabase.getInstance().getReference("users");
        //mFirebaseInstance.getReference("quiztree-11838").setValue("Realtime Database");
        //ScrollView l=(ScrollView)findViewById(R.id.backgrnd);
        //l.getBackground().setAlpha(50);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = _nameText.getText().toString();
                String grade = _gradeText.getText().toString();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Please enter your name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(grade)) {
                    Toast.makeText(getApplicationContext(), "Enter your class!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(grade) < 1 || Integer.parseInt(grade) > 12) {
                    Toast.makeText(getApplicationContext(), "Enter valid grade between 1 to 12!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d(TAG, "user created!");
                                    onAuthSuccess(task.getResult().getUser());
                                }
                            }
                        });

            }
        });

    }

    private void onAuthSuccess(FirebaseUser userC) {
        Log.d(TAG, "in onAuthSuccess");
        String name=_nameText.getText().toString();
        String  grade=_gradeText.getText().toString();
        String userId=userC.getUid();
        User user = new User(name, grade,userC.getEmail());

        mDatabase.child(userId).setValue(user);
        // Write new user

        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

    public void onBackPressed(){
        moveTaskToBack(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


}

