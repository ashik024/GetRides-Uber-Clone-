package com.example.getrides;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DriverLogin extends AppCompatActivity {

    EditText email;
    EditText pass;
    Button login;
    Button register;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        mAuth= FirebaseAuth.getInstance();

        email = findViewById(R.id.Driveremail);
        pass = findViewById(R.id.Driverpassword);
        login= findViewById(R.id.logindriver);
        register= findViewById(R.id.signupdriver);

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user!= null){
//                    Intent intent = new Intent(DriverLogin.this,MainActivity2.class);
//                    startActivity(intent);
//                }
//            }
//        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.getrides.DriverLogin.this,DriverRegister.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Driverlogin();

            }
        });

    }

    private void Driverlogin() {
        String DemailL = email.getText().toString().trim();
        String DpasswordL = pass.getText().toString().trim();


        if(TextUtils.isEmpty(DemailL)){

            Toast.makeText(com.example.getrides.DriverLogin.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(DpasswordL)){

            Toast.makeText(com.example.getrides.DriverLogin.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }

        mAuth.signInWithEmailAndPassword(DemailL,DpasswordL).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()){

                    Toast.makeText(com.example.getrides.DriverLogin.this, "Login Error", Toast.LENGTH_SHORT).show();
                }
                if (task.isSuccessful()){

                    Intent intent = new Intent(com.example.getrides.DriverLogin.this, DriverMap.class);
                    startActivity(intent);
                }

            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        mAuth.removeAuthStateListener(authStateListener);
//    }
}