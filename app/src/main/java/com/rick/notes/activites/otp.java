package com.rick.notes.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.rick.notes.R;

public class otp extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4;
    private ScrollView Snackie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        Button ToLogin = findViewById(R.id.backToLogin);
        Button NextClick = findViewById(R.id.next);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        ToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Already = new Intent(otp .this, signup.class);
                startActivity(Already);
            }
        });

        NextClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpstr1 =otp1.getText().toString();
                String otpstr2 =otp2.getText().toString();
                String otpstr3 =otp3.getText().toString();
                String otpstr4 =otp4.getText().toString();
                String WholeOTP = otpstr1+otpstr2+otpstr3+otpstr4;
                String rightOTP =  "3355";

                if(WholeOTP.equals(rightOTP)){
                    Toast.makeText(getApplicationContext(),WholeOTP,Toast.LENGTH_SHORT).show();
                    Intent Already = new Intent(otp .this, MainActivity.class);
                    startActivity(Already);
                }else{
                    Toast.makeText(getApplicationContext(),WholeOTP+" OTP is wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}