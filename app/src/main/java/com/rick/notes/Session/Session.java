package com.rick.notes.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setEmailId(String emailId) {
        prefs.edit().putString("emailId", emailId).commit();
    }

    public void setFirstName(String firstName) {
        prefs.edit().putString("firstName", firstName).commit();
    }

    public String getEmailId() {
        String emailId = prefs.getString("emailId","");
        return emailId;
    }

    public String getFirstName() {
        String firstName = prefs.getString("firstName","");
        return firstName;
    }
}
