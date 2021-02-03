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
import com.google.firebase.database.FirebaseDatabase;

public class PassengerRegister extends AppCompatActivity {

    EditText name;
    EditText phone;
    EditText email;
    EditText pass;


    Button register;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_register);


        name = findViewById(R.id.PassengerNameR);
        phone = findViewById(R.id.PassengerPhoneR);
        email = findViewById(R.id.Passengeremail);
        pass = findViewById(R.id.PassengerpasswordR);
        mauth=FirebaseAuth.getInstance();


        register = findViewById(R.id.RegisterPassengerR);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passengerregister();

            }
        });
    }

    private void passengerregister() {

        String Pname = name.getText().toString();
        String Pphone    = phone.getText().toString();
        String Pemail = email.getText().toString().trim();
        String Ppassword = pass.getText().toString().trim();

        if(TextUtils.isEmpty(Pname)){

            Toast.makeText(com.example.getrides.PassengerRegister.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Pphone)){

            Toast.makeText(com.example.getrides.PassengerRegister.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(Pemail)){

            Toast.makeText(com.example.getrides.PassengerRegister.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(Ppassword)){

            Toast.makeText(com.example.getrides.PassengerRegister.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }


        if(Ppassword.length() < 6){
            Toast.makeText(com.example.getrides.PassengerRegister.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }

        mauth.createUserWithEmailAndPassword(Pemail,Ppassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    PassengerInfo passengerInfo = new PassengerInfo(Pname,Pphone,Pemail);

                    String userid = mauth.getCurrentUser().getUid();
                     FirebaseDatabase.getInstance().getReference().child("user").child("passenger").child(userid).setValue(passengerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {

                             if (task.isSuccessful()){

                                 Toast.makeText(com.example.getrides.PassengerRegister.this, "Registration Completed ", Toast.LENGTH_SHORT).show();

                                 Intent intent = new Intent(com.example.getrides.PassengerRegister.this, com.example.getrides.PassengerLogin.class);
                                 startActivity(intent);

                             }else {
                                 Toast.makeText(com.example.getrides.PassengerRegister.this, "Registration Error ", Toast.LENGTH_SHORT).show();

                             }

                         }
                     });

                }else {

                    Toast.makeText(com.example.getrides.PassengerRegister.this, "Error ", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}