package com.rick.notes.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.rick.notes.R;
import com.rick.notes.Session.Session;
import com.rick.notes.database.UserDatabaseSQLite;

public class RegistrationActivity extends AppCompatActivity {

    private Button Login, OTP;
    private EditText emailIdET, firstNameET, lastNameET, passwordET, retypePasswordET;
    private UserDatabaseSQLite userDatabaseSqLite;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ImageButton backarrow = (ImageButton) findViewById(R.id.backarrow);
        Login = (Button) findViewById(R.id.Already_login);
        OTP = (Button) findViewById(R.id.otpPage);
        emailIdET = (EditText) findViewById(R.id.emailIdET);
        firstNameET = (EditText) findViewById(R.id.firstNameET);
        lastNameET = (EditText) findViewById(R.id.lastNameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        retypePasswordET = (EditText) findViewById(R.id.retypePasswordET);
        userDatabaseSqLite = new UserDatabaseSQLite(getApplicationContext());
        session = new Session(getApplicationContext());

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Already = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(Already);
            }
        });

        OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameET.getText().toString().trim();
                String lastName = lastNameET.getText().toString().trim();
                String emailId = emailIdET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String retypePassword = retypePasswordET.getText().toString().trim();

                if(!"".equalsIgnoreCase(firstName)
                        && !"".equalsIgnoreCase(lastName)
                        && !"".equalsIgnoreCase(emailId)
                        && !"".equalsIgnoreCase(password)
                ){
                    //Check for duplicate value
                    if(userDatabaseSqLite.checkIfUserExists(emailId)){
                        emailIdET.setError("Email Id already registered!");
                        return;
                    }

                    //Check for minimum length of password
                    if(password.length() < 7 || password.length() > 15){
                        passwordET.setError("Password length should be 7 to 15.");
                        return;
                    }

                    if(password.equals(retypePassword)){
                        session.setEmailId(emailId);
                        session.setFirstName(firstName);

                        Intent intent = new Intent(RegistrationActivity.this, OTPActivity.class);
                        intent.putExtra("emailId", emailId);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("lastName", lastName);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }else{
                        retypePasswordET.setError("Please re-enter password");
                    }
                }else{
                    if("".equalsIgnoreCase(firstName))
                        firstNameET.setError("Please enter first name");

                    if("".equalsIgnoreCase(lastName))
                        lastNameET.setError("Please enter last name");

                    if("".equalsIgnoreCase(emailId))
                        emailIdET.setError("Please enter email id");

                    if("".equalsIgnoreCase(password))
                        passwordET.setError("Please enter password");
                }
            }
        });
    }
}