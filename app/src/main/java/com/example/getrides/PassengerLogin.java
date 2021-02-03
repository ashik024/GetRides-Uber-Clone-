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

public class PassengerLogin extends AppCompatActivity {

    EditText email;
    EditText pass;
    Button login;
    Button register;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_login);

        email = findViewById(R.id.Passengeremail);
        pass = findViewById(R.id.Passengerpassword);
        login= findViewById(R.id.loginPassenger);
        register= findViewById(R.id.signupPassenger);

        mAuth= FirebaseAuth.getInstance();

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user!= null){
//                    Intent intent = new Intent(PassengerLogin.this,MainActivity2.class);
//                    startActivity(intent);
//                }
//            }
//        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.getrides.PassengerLogin.this,PassengerRegister.class);
                startActivity(intent);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passerngerlogin();

            }
        });
    }

    private void passerngerlogin() {
        String PemailL = email.getText().toString().trim();
        String PpasswordL = pass.getText().toString().trim();


        if(TextUtils.isEmpty(PemailL)){

            Toast.makeText(com.example.getrides.PassengerLogin.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(PpasswordL)){

            Toast.makeText(com.example.getrides.PassengerLogin.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }

        mAuth.signInWithEmailAndPassword(PemailL,PpasswordL).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()){

                    Toast.makeText(com.example.getrides.PassengerLogin.this, "Login Error", Toast.LENGTH_SHORT).show();
                } if (task.isSuccessful()){

                    Intent intent = new Intent(com.example.getrides.PassengerLogin.this, PassengerMap.class);
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