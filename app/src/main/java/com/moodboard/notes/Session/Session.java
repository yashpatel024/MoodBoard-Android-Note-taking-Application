package com.moodboard.notes.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences sharedPreferences;

    public Session(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setEmailId(String emailId) {
        sharedPreferences.edit().putString("emailId", emailId).commit();
    }

    public void setFirstName(String firstName) {
        sharedPreferences.edit().putString("firstName", firstName).commit();
    }

    public String getEmailId() {
        String emailId = sharedPreferences.getString("emailId","");
        return emailId;
    }

    public String getFirstName() {
        String firstName = sharedPreferences.getString("firstName","");
        return firstName;
    }
}
