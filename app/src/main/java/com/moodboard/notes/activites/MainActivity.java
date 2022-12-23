package com.moodboard.notes.activites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.moodboard.notes.R;
import com.moodboard.notes.Session.Session;
import com.moodboard.notes.adapters.NotesAdapter;
import com.moodboard.notes.database.NotesDatabase;
import com.moodboard.notes.database.UserDatabaseSQLite;
import com.moodboard.notes.entities.Notes;
import com.moodboard.notes.listener.NotesListener;
import com.moodboard.notes.contants.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements NotesListener {

    //Variable Declaration
    private RecyclerView notesRecyclerView;
    private List<Notes> noteList;
    private NotesAdapter notesAdapter;
    private TextView textMyNotes;
    private int noteClickedPosition = -1;
    ImageView imageAddNoteMain;
    private AlertDialog dialogAddURL;
    EditText inputSearch;

    //Session Variables
    private Session session;
    private UserDatabaseSQLite userDatabaseSqLite;
    private String sessionUserName, sessionUserEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    void init(){
        initializeVariables();
        inputSearchListener();
    }

    void initializeVariables(){
        //Variable Initialization
        userDatabaseSqLite = new UserDatabaseSQLite(getApplicationContext());
        session = new Session(getApplicationContext());
        sessionUserName = session.getFirstName();
        sessionUserEmailId = session.getEmailId();

        //Change home page greetings
        textMyNotes = (TextView) findViewById(R.id.textMyNotes);
        textMyNotes.setText("Hello, "+sessionUserName);

        //Recycler View config
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        notesRecyclerView.setAdapter(notesAdapter);

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(v -> startActivityForResult(
                new Intent(getApplicationContext(), CreateNoteActivity.class), Constant.REQUEST_CODE_ADD_NOTE)
        );

        //Get notes for sessionEmailId
        getNotes(Constant.REQUEST_CODE_SHOW_NOTES, false);

        //Search field initialization
        inputSearch = findViewById(R.id.inputSearch);
    }

    void inputSearchListener(){
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (noteList.size() != 0) {
                    notesAdapter.searchNotes(s.toString());
                }
            }
        });
    }

    /*
    On Click of notes, start new Activity with update more
     */
    @Override
    public void onNoteClicked(Notes note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, Constant.REQUEST_CODE_UPDATE_NOTE);
    }

    /*
       On different action on notes,
       asynchornous task
        Update, delete, add, fetch - notifying NoteAdapters
     */
    private void getNotes(final int requestCode, final boolean isNoteDeleted) {

        @SuppressLint("StaticFieldLeak")
        class GetNoteTask extends AsyncTask<Void, Void, List<Notes>> {


            @Override
            protected List<Notes> doInBackground(Void... voids) {

                return NotesDatabase.getNotesDatabase(getApplicationContext())
                        .noteDao().getAllNotes(sessionUserEmailId);
            }

            @Override
            protected void onPostExecute(List<Notes> notes) {
                super.onPostExecute(notes);
                if (requestCode == Constant.REQUEST_CODE_SHOW_NOTES) {
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                } else if (requestCode == Constant.REQUEST_CODE_ADD_NOTE) {
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == Constant.REQUEST_CODE_UPDATE_NOTE) {
                    noteList.remove(noteClickedPosition);
                    if (isNoteDeleted) {
                        notesAdapter.notifyItemRemoved(noteClickedPosition);
                    } else {
                        noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                        notesAdapter.notifyItemChanged(noteClickedPosition);
                    }
                }
            }
        }

        new GetNoteTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(Constant.REQUEST_CODE_ADD_NOTE, false);
        } else if (requestCode == Constant.REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getNotes(Constant.REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            }
        } else if (requestCode == Constant.REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        String selectedImagePath = getPathFromUri(selectedImageUri);
                        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                        intent.putExtra("isFromQuickActions", true);
                        intent.putExtra("quickActionType", "image");
                        intent.putExtra("imagePath", selectedImagePath);
                        startActivityForResult(intent, Constant.REQUEST_CODE_ADD_NOTE);
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }
}