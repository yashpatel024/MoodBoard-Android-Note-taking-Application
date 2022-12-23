package com.moodboard.notes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.moodboard.notes.DataAccess.NoteDataAccess;
import com.moodboard.notes.contants.Constant;
import com.moodboard.notes.entities.Notes;

@Database(entities = Notes.class, version = 2, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    private static NotesDatabase notesDatabase;

    public static synchronized NotesDatabase getNotesDatabase(Context context) {
        if (notesDatabase == null)
            notesDatabase = Room.databaseBuilder(context, NotesDatabase.class, Constant.NOTES_DATABASE_NAME).build();

        return notesDatabase;
    }

    public abstract NoteDataAccess noteDao();
}
