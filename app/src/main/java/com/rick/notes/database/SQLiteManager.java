package com.rick.notes.database;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rick.notes.entities.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;

    private static final String DATABASE_NAME ="NotesDB";
    private static final int DATABASE_VERSION = 1;

    //Notes Table
    private static final String TABLE1_NAME = "Notes";
    private static final String EMAIL ="email";
    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String NOTES_TEXT = "text";
    private static final String SUB_TITLE = "sub";
    private static final String IMAGE_PATH = "image";
    private static final String COLOR = "color";
    private static final String WEB_LINK = "link";

    //Users Table
    private static final String TABLE2_NAME = "Users";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String USER_EMAIL = "email";
    private static final String Password = "password";



    @SuppressLint("SimpleDateFormat")
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");



    public SQLiteManager(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context){
        if(sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);
        return sqLiteManager;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql1;
        sql1 = new StringBuilder()
                .append("CREATE TABLE")
                .append(TABLE1_NAME)
                .append("(")
                .append(EMAIL)
                .append(" TEXT PRIMARY KEY, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(NOTES_TEXT)
                .append(" TEXT, ")
                .append(IMAGE_PATH)
                .append(" TEXT, ")
                .append(COLOR)
                .append(" TEXT, ")
                .append(WEB_LINK)
                .append(" TEXT, ")
                .append(SUB_TITLE)
                .append(" TEXT)");

        StringBuilder sql2;
        sql2 = new StringBuilder()
                .append("CREATE TABLE")
                .append(TABLE2_NAME)
                .append("(")
                .append(FIRST_NAME)
                .append(" TEXT, ")
                .append(LAST_NAME)
                .append(" TEXT, ")
                .append(USER_EMAIL)
                .append(" TEXT, ")
                .append(Password)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql1.toString());
        sqLiteDatabase.execSQL(sql2.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //GET USER
    public void getUser(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE2_NAME, null)) {
            if(result.getCount() !=0){
                while(result.moveToNext()){
                    String firstName = result.getString(1);
                    String lastName = result.getString(2);
                    String email = result.getString(3);

                    //   User user = new Note(firstName, lastName, email);
                    //    User.userArrayList.add(user);
                }
            }
        }

    }

    public void addNoteToDatabase(Note note){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, note.getId());
        contentValues.put(TITLE_FIELD, note.getTitle());
        contentValues.put(SUB_TITLE, note.getSubtitle());
        contentValues.put(NOTES_TEXT, note.getNoteText());
        contentValues.put(IMAGE_PATH, note.getImagePath());
        contentValues.put(COLOR, note.getColor());
        contentValues.put(WEB_LINK, note.getWebLink());

        sqLiteDatabase.insert(TABLE1_NAME, null, contentValues);

        getAllNotes();

    }

    public List<Note> getAllNotes(){
        List<Note> notes = new ArrayList<Note>();
        String selectQuery = "SELECT * FROM "+ TABLE2_NAME;

        Log.e(String.valueOf(LOG), selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                Note printNotes = new Note();
                printNotes.setId(c.getInt((c.getColumnIndex(ID_FIELD))));
                printNotes.setTitle(c.getString((c.getColumnIndex(TITLE_FIELD))));
                printNotes.setSubtitle(c.getString((c.getColumnIndex(SUB_TITLE))));
                printNotes.setNoteText(c.getString((c.getColumnIndex(NOTES_TEXT))));
                printNotes.setColor(c.getString((c.getColumnIndex(COLOR))));
                printNotes.setWebLink(c.getString((c.getColumnIndex(WEB_LINK))));
                printNotes.setImagePath(c.getString((c.getColumnIndex(IMAGE_PATH))));

                notes.add(printNotes);
            }while(c.moveToNext());

            System.out.println(Arrays.toString(notes.toArray()));
        }
        return notes;
    }



    public void updateNoteInDB(Note note){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, note.getId());
        contentValues.put(TITLE_FIELD, note.getTitle());
        contentValues.put(SUB_TITLE, note.getSubtitle());
        contentValues.put(NOTES_TEXT, note.getNoteText());
        contentValues.put(IMAGE_PATH, note.getImagePath());
        contentValues.put(COLOR, note.getColor());
        contentValues.put(WEB_LINK, note.getWebLink());

      //  sqLiteDatabase.insert(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(note.getId())});

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
