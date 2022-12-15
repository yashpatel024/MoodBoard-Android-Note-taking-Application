package com.rick.notes.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.rick.notes.R;
import com.rick.notes.activites.Login;
import com.rick.notes.activites.otp;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ImageButton backarrow = (ImageButton) findViewById(R.id.backarrow);
        Button Login = (Button) findViewById(R.id.Already_login);
        Button OTP = (Button) findViewById(R.id.otpPage);


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, com.rick.notes.activites.Login .class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Already = new Intent(signup.this, com.rick.notes.activites.Login .class);
                startActivity(Already);
            }
        });

        OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Already = new Intent(signup.this, com.rick.notes.activites.otp.class);
                startActivity(Already);
            }
        });
    }
}