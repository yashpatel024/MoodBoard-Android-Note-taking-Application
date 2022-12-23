package com.moodboard.notes.DataAccess;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.moodboard.notes.entities.Notes;

import java.util.List;

@Dao
public interface NoteDataAccess {

    /*
        Get All notes based on LoggedIn user
     */
    @Query("SELECT * FROM notes WHERE emailId = :emailId ORDER BY id DESC")
    List<Notes> getAllNotes(String emailId);

    /*
    Insert Note, using Entity Note
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Notes note);

    /*
    Delete Note
     */
    @Delete
    void deleteNote(Notes note);
}
