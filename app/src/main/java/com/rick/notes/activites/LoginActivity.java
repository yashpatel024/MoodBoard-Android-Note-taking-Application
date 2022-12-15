package com.rick.notes.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rick.notes.R;
import com.rick.notes.Session.Session;
import com.rick.notes.database.UserDatabaseSQLite;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn, forgetPasswordbtn, signupBtn;
    private EditText emailIdET, passwordET;
    private UserDatabaseSQLite userDatabaseSqLite;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        forgetPasswordbtn = (Button) findViewById(R.id.forgetPasswordbtn);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        emailIdET = (EditText) findViewById(R.id.emailIdET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        userDatabaseSqLite = new UserDatabaseSQLite(getApplicationContext());
        session = new Session(getApplicationContext());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailId = emailIdET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if(!"".equalsIgnoreCase(emailId)
                        && !"".equalsIgnoreCase(password)
                ){
                    //Check for Credentials
                    HashMap<String, String> validation = userDatabaseSqLite.checkUserCredentials(emailId, password);

                    if("yes".equalsIgnoreCase(validation.get("error")) || validation.get("error") == null){
                        emailIdET.setError(validation.get("message"));
                        return;
                    }else {
                        Log.i("login", "logged in");
                        session.setEmailId(emailId);
                        session.setFirstName(validation.get("firstName"));

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }else{
                    if("".equalsIgnoreCase(emailId))
                        emailIdET.setError("Please enter emailId");

                    if("".equalsIgnoreCase(password))
                        passwordET.setError("Please enter password");
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        forgetPasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }
}