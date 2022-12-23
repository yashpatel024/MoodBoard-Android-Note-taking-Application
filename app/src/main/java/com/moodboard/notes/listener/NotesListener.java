package com.moodboard.notes.listener;

import com.moodboard.notes.entities.Notes;

public interface NotesListener {
    void onNoteClicked(Notes note, int position);
}
