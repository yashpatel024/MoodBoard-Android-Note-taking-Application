package com.rick.notes.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rick.notes.entities.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;

    private static final String DATABASE_NAME ="NotesDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Notes";
    private static final String COUNTER ="Counter";

    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String NOTES_TEXT = "text";
    private static final String SUB_TITLE = "sub";
    private static final String IMAGE_PATH = "image";
    private static final String COLOR = "color";
    private static final String WEB_LINK = "link";


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
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
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

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

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


        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populatedNoteListArray(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if(result.getCount() !=0){
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    String note_txt = result.getString(3);
                    String sub_title = result.getString(4);
                    String color = result.getString(5);
                    String image = result.getString(6);
                    String web_link = result.getString(7);

                 //   Note note = new Note(id, title, note_txt, sub_title, color, image, web_link );
                //    Note.noteArrayList.add(note);
                }
            }
        }

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
