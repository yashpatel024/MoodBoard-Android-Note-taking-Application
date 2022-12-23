package com.moodboard.notes.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.moodboard.notes.entities.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UserDatabaseSQLite extends SQLiteOpenHelper {

    private static UserDatabaseSQLite userDatabaseSqLite;

    private static final String DATABASE_NAME ="UserDatabase";
    private static final int DATABASE_VERSION = 2;

    //Users Table
    private static final String TABLE_NAME = "Users";
    private static final String USER_EMAIL = "email";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PASSWORD = "password";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public UserDatabaseSQLite(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static UserDatabaseSQLite instanceOfDatabase(Context context){
        if(userDatabaseSqLite == null)
            userDatabaseSqLite = new UserDatabaseSQLite(context);
        return userDatabaseSqLite;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        StringBuilder sql1;
        sql1 = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(USER_EMAIL)
                .append(" TEXT PRIMARY KEY, ")
                .append(FIRST_NAME)
                .append(" TEXT NOT NULL, ")
                .append(LAST_NAME)
                .append(" TEXT, ")
                .append(PASSWORD)
                .append(" TEXT NOT NULL)");

        sqLiteDatabase.execSQL(sql1.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //Insert New USer
    public boolean insertUser(User user){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(USER_EMAIL, user.getEmailId());
        cv.put(FIRST_NAME, user.getFirstName());
        cv.put(LAST_NAME, user.getLastName());
        cv.put(PASSWORD, user.getPassword());

        long result = sqLiteDatabase.insert(TABLE_NAME, null, cv);
        return ((result==-1)? false: true);
    }

    //Check User Credentials - LOGIN
    public HashMap<String, String> checkUserCredentials(String emailId, String password){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        HashMap<String, String> return_map = new HashMap<>();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + USER_EMAIL + " = '" +emailId +"';", null)) {
            if(result.getCount() !=0){
                while(result.moveToNext()){
                    Log.i("result", String.valueOf(result));
                    String email = result.getString(0);
                    String firstName = result.getString(1);
                    String lastName = result.getString(2);

                    if(password.equals(result.getString(3))){
                        return_map.put("error", "no");
                        return_map.put("firstName", firstName);
                    }else{
                        return_map.put("error", "yes");
                        return_map.put("message", "Email id or password is incorrect");
                    }
                }
            }else{
                return_map.put("error", "yes");
                return_map.put("message", "User not found");
            }
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
        }

        return return_map;
    }

    //Check for existing user
    public boolean checkIfUserExists(String enteredEmail){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + USER_EMAIL + " = '" +enteredEmail +"';", null)) {
            Log.i("checkIfUserExists", String.valueOf(result));
            if(result.getCount() !=0)
                return true;
            return false;
        }
    }

    private String getStringFromDate(Date date){
        if(date==null)
            return null;
        return DATE_FORMAT.format(date);
    }

    private Date getDateFromString(String string){
        try {
            return DATE_FORMAT.parse(string);
        }
        catch (ParseException | NullPointerException e){
            return null;
        }
    }
}
