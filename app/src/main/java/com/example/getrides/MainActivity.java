package com.example.getrides;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button Passenger;
    Button Driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Passenger= findViewById(R.id.user);
        Driver= findViewById(R.id.user2);

        Passenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.getrides.MainActivity.this, com.example.getrides.PassengerLogin.class);
                startActivity(intent);
                finish();
            }
        });

        Driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.getrides.MainActivity.this, com.example.getrides.DriverLogin.class);
                startActivity(intent);
                finish();
            }
        });

    }
}