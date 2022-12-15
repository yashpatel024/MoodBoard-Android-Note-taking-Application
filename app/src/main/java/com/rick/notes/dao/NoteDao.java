package com.rick.notes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rick.notes.entities.Notes;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes WHERE emailId = :emailId ORDER BY id DESC")
    List<Notes> getAllNotes(String emailId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Notes note);

    @Delete
    void deleteNote(Notes note);
}
