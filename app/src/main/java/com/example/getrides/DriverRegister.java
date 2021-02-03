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

public class DriverRegister extends AppCompatActivity {



    EditText name;
    EditText phone;
    EditText email;
    EditText pass;
    EditText carNumber;

    Button register;

    FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);

        mauth=FirebaseAuth.getInstance();

        name = findViewById(R.id.DriverNameR);
        phone = findViewById(R.id.DriverPhoneR);
        email = findViewById(R.id.Driveremail);
        pass = findViewById(R.id.DriverpasswordR);
        carNumber = findViewById(R.id.DriverNumberPlateR);

        register = findViewById(R.id.RegisterDriverR);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerdrivers();


    }

    private void registerdrivers() {
        String Dname = name.getText().toString();
        String Dphone    = phone.getText().toString();
        String Demail = email.getText().toString().trim();
        String Dpassword = pass.getText().toString().trim();
        String DcarNumber = carNumber.getText().toString().trim();

        if(TextUtils.isEmpty(Dname)){

            Toast.makeText(com.example.getrides.DriverRegister.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Dphone)){

            Toast.makeText(com.example.getrides.DriverRegister.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(Demail)){

            Toast.makeText(com.example.getrides.DriverRegister.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(Dpassword)){

            Toast.makeText(com.example.getrides.DriverRegister.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(DcarNumber)){

            Toast.makeText(com.example.getrides.DriverRegister.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }

        if(Dpassword.length() < 6){
            Toast.makeText(com.example.getrides.DriverRegister.this, "Complete All Fields", Toast.LENGTH_SHORT).show();
        }

        mauth.createUserWithEmailAndPassword(Demail,Dpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()){

                    com.example.getrides.DriverInfo driverInfo = new com.example.getrides.DriverInfo(Dname,Dphone,Demail,DcarNumber);

                    String userid = mauth.getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("user").child("driver").child(userid).setValue(driverInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(com.example.getrides.DriverRegister.this, "Registration Completed ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(com.example.getrides.DriverRegister.this, com.example.getrides.DriverLogin.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(com.example.getrides.DriverRegister.this, "Registration Error ", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }else {

                    Toast.makeText(com.example.getrides.DriverRegister.this, " Error ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
        });
    }

}