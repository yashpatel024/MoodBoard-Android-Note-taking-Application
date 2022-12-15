package com.rick.notes.Session.listeners;

import com.rick.notes.entities.Notes;

public interface NotesListener {
    void onNoteClicked(Notes note, int position);
}
