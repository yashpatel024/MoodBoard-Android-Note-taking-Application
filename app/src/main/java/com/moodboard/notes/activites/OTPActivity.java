package com.moodboard.notes.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.moodboard.notes.R;
import com.moodboard.notes.database.UserDatabaseSQLite;
import com.moodboard.notes.entities.User;

public class OTPActivity extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4;
    private EditText[] otpTexts;
    private UserDatabaseSQLite userDatabaseSqLite;
    private TextView otpEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        userDatabaseSqLite = new UserDatabaseSQLite(getApplicationContext());

        Intent intent = getIntent();
        String emailId = intent.getStringExtra("emailId");
        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        String password = intent.getStringExtra("password");

        //Create new User Object
        User newUser = new User(
                emailId, firstName, lastName, password);

        Button ToLogin = findViewById(R.id.backToLogin);
        Button NextClick = findViewById(R.id.next);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);

        otpTexts = new EditText[]{otp1, otp2, otp3, otp4};

        otp1.addTextChangedListener(new PinTextWatcher(0));
        otp2.addTextChangedListener(new PinTextWatcher(1));
        otp3.addTextChangedListener(new PinTextWatcher(2));
        otp4.addTextChangedListener(new PinTextWatcher(3));

        otp1.setOnKeyListener(new PinOnKeyListener(0));
        otp2.setOnKeyListener(new PinOnKeyListener(1));
        otp3.setOnKeyListener(new PinOnKeyListener(2));
        otp4.setOnKeyListener(new PinOnKeyListener(3));

        otpEmailId = (TextView) findViewById(R.id.otpEmailId);

        otpEmailId.setText(emailId);

        ToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Already = new Intent(OTPActivity.this, RegistrationActivity.class);
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
//                    Toast.makeText(getApplicationContext(),WholeOTP,Toast.LENGTH_SHORT).show();

                    if(!userDatabaseSqLite.insertUser(newUser)){
                        Log.e("Insert user", "User not inserted");
                        return;
                    }

                    Intent Already = new Intent(OTPActivity.this, MainActivity.class);
                    startActivity(Already);
                }else if(WholeOTP.trim().length() < 4){
                    Toast.makeText(getApplicationContext(),"Enter complete OTP",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"OTP is wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == otpTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            otpTexts[currentIndex].removeTextChangedListener(this);
            otpTexts[currentIndex].setText(text);
            otpTexts[currentIndex].setSelection(text.length());
            otpTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                otpTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                otpTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                otpTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : otpTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                if (otpTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    otpTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }
}