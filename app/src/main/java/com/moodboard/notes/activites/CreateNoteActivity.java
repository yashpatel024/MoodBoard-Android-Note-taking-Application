package com.moodboard.notes.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.moodboard.notes.R;
import com.moodboard.notes.Session.Session;
import com.moodboard.notes.contants.Constant;
import com.moodboard.notes.database.NotesDatabase;
import com.moodboard.notes.entities.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {

    //Declaration of variables
    private EditText inputTitleET, inputSubTitleET, inputContentET;
    private TextView noteDateTimeTV;
    private View subtitleView;
    private ImageView imageViewOfNoteIV;
    private TextView urlOfNoteET;
    private LinearLayout layoutWebURLLL;

    private String selectedColor;
    private String selectedImagePath;

    private AlertDialog dialogAddURL;
    private AlertDialog dialogDeleteNote;

    private Notes currentNote;
    private Session session;
    private String sessionUserEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        init();
    }

    void init(){
        initializeVariables();
        initMiscellaneous();
        setSubtitleIndicatorColor();
    }

    void initializeVariables(){
        //Initialization
        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        inputTitleET = findViewById(R.id.inputNoteTitle);
        inputSubTitleET = findViewById(R.id.inputNoteSubtitle);
        inputContentET = findViewById(R.id.inputNoteText);
        subtitleView = findViewById(R.id.viewSubtitleIndicator);
        imageViewOfNoteIV = findViewById(R.id.imageNote);
        urlOfNoteET = findViewById(R.id.textWebURL);
        layoutWebURLLL = findViewById(R.id.layoutWebURL);

        selectedColor = "#333333";
        selectedImagePath = "";

        session = new Session(getApplicationContext());
        sessionUserEmailId = session.getEmailId();

        noteDateTimeTV = findViewById(R.id.textDateTime);
        noteDateTimeTV.setText(new SimpleDateFormat(
                "EEEE, dd MMMM yyyy HH:mm a",
                Locale.getDefault()).format(new Date().getTime())
        );

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(v -> saveNote());


        //Check for Update mode or Create mode
        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            currentNote = (Notes) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }

        findViewById(R.id.imageRemoveWebURL).setOnClickListener(v -> {
            urlOfNoteET.setText(null);
            layoutWebURLLL.setVisibility(View.GONE);
        });

        findViewById(R.id.imageRemoveImage).setOnClickListener(v -> {
            imageViewOfNoteIV.setImageBitmap(null);
            imageViewOfNoteIV.setVisibility(View.GONE);
            findViewById(R.id.imageRemoveImage).setVisibility(View.GONE);
            selectedImagePath = "";
        });

        if (getIntent().getBooleanExtra("isFromQuickActions", false)) {
            String type = getIntent().getStringExtra("quickActionType");
            if (type != null) {
                if (type.equals("image")) {
                    selectedImagePath = getIntent().getStringExtra("imagePath");
                    imageViewOfNoteIV.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                    imageViewOfNoteIV.setVisibility(View.VISIBLE);
                    findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);
                } else if (type.equals("URL")) {
                    urlOfNoteET.setText(getIntent().getStringExtra("URL"));
                    layoutWebURLLL.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /*
        - Initialize Edit mode
        - Material Ui component for sheet behaviour in bottom
     */
    private void initMiscellaneous() {
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutEditNote);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);

        layoutMiscellaneous.findViewById(R.id.textEditNote).setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);

        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(v -> {
            selectedColor = "#333333";
            imageColor1.setImageResource(R.drawable.ic_done);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(v -> {
            selectedColor = "#FDBE3B";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(R.drawable.ic_done);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(v -> {
            selectedColor = "#FF4842";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(R.drawable.ic_done);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(v -> {
            selectedColor = "#3A52FC";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(R.drawable.ic_done);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(v -> {
            selectedColor = "#000000";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(R.drawable.ic_done);
            setSubtitleIndicatorColor();
        });

        if (currentNote != null) {
            final String noteColorCode = currentNote.getColor();
            if (noteColorCode != null && !noteColorCode.trim().isEmpty()) {
                switch (noteColorCode) {
                    case "#FDBE3B":
                        layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                        break;
                    case "#FF4842":
                        layoutMiscellaneous.findViewById(R.id.viewColor3).performClick();
                        break;
                    case "#3A52FC":
                        layoutMiscellaneous.findViewById(R.id.viewColor4).performClick();
                        break;
                    case "#000000":
                        layoutMiscellaneous.findViewById(R.id.viewColor5).performClick();
                        break;
                }
            }
        }

        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CreateNoteActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQUEST_CODE_STORAGE_PERMISSION);
            } else {
                selectImage();
            }
        });

        layoutMiscellaneous.findViewById(R.id.layoutAddUrl).setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            showAddURLDialog();
        });

        if (currentNote != null) {
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setOnClickListener(v -> {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showDeleteNoteDialog();
            });
        }
    }

    /*
    Show delete in case of Update mode of note
     */
    private void showDeleteNoteDialog() {
        if (dialogDeleteNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if (dialogDeleteNote.getWindow() != null) {
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(v -> {

                @SuppressLint("StaticFieldLeak")
                class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().deleteNote(currentNote);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Intent intent = new Intent();
                        intent.putExtra("isNoteDeleted", true);
                        setResult(RESULT_OK, intent);

                        dialogDeleteNote.dismiss();
                        finish();
                    }
                }

                new DeleteNoteTask().execute();
            });

            view.findViewById(R.id.textCancel).setOnClickListener(v -> dialogDeleteNote.dismiss());
        }

        dialogDeleteNote.show();
    }

    /*
        - Indicator of Color theme in subtitle
     */
    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) subtitleView.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedColor));
    }

    /*
    Open Media folder
     */
    @SuppressLint("QueryPermissionsNeeded")
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, Constant.REQUEST_CODE_SELECT_IMAGE);
        }
    }

    /*
    Permission to access Media storage
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    Get image path (URI)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        Glide.with(CreateNoteActivity.this)
                                .load(selectedImageUri)
                                .into(imageViewOfNoteIV);

                        imageViewOfNoteIV.setVisibility(View.VISIBLE);
                        findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);

                        selectedImagePath = getPathFromUri(selectedImageUri);
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

    /*
        URL dialog using alertDialog
     */
    private void showAddURLDialog() {
        if (dialogAddURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.layout_add_url, findViewById(R.id.layoutAddUrlContainer));
            builder.setView(view);

            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null) {
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputURL = view.findViewById(R.id.inputURL);
            inputURL.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(v -> {
                final String inputURLStr = inputURL.getText().toString().trim();

                if (inputURLStr.isEmpty()) {
                    Toast.makeText(CreateNoteActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.WEB_URL.matcher(inputURLStr).matches()) {
                    Toast.makeText(CreateNoteActivity.this, "Enter valid URL", Toast.LENGTH_SHORT).show();
                } else {
                    urlOfNoteET.setText(inputURL.getText().toString());
                    layoutWebURLLL.setVisibility(View.VISIBLE);
                    dialogAddURL.dismiss();
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(v -> dialogAddURL.dismiss());
        }
        dialogAddURL.show();
    }

    private void setViewOrUpdateNote() {
        inputTitleET.setText(currentNote.getTitle());
        inputSubTitleET.setText(currentNote.getSubtitle());
        inputContentET.setText(currentNote.getNoteText());
        noteDateTimeTV.setText(currentNote.getDateTime());

        final String imagePathStr = currentNote.getImagePath();
        if (imagePathStr != null && !imagePathStr.trim().isEmpty()) {
            imageViewOfNoteIV.setImageBitmap(BitmapFactory.decodeFile(imagePathStr));
            imageViewOfNoteIV.setVisibility(View.VISIBLE);
            findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);
            selectedImagePath = imagePathStr;
        }

        final String webLinkStr = currentNote.getWebLink();
        if (webLinkStr != null && !webLinkStr.trim().isEmpty()) {
            urlOfNoteET.setText(currentNote.getWebLink());
            layoutWebURLLL.setVisibility(View.VISIBLE);
        }
    }

    /*
    Save Note to save in db update/create
    Validation for empty field will be checked
     */
    private void saveNote() {
        final String noteTitle = inputTitleET.getText().toString().trim();
        final String noteSubtitle = inputSubTitleET.getText().toString().trim();
        final String noteText = inputContentET.getText().toString().trim();
        final String dateTimeStr = noteDateTimeTV.getText().toString().trim();

        if (noteTitle.isEmpty()) {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else if (noteSubtitle.isEmpty() && noteText.isEmpty()) {
            Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        final Notes note = new Notes();
        note.setEmailId(sessionUserEmailId);
        note.setTitle(noteTitle);
        note.setSubtitle(noteSubtitle);
        note.setNoteText(noteText);
        note.setDateTime(dateTimeStr);
        note.setColor(selectedColor);
        note.setImagePath(selectedImagePath);

        if (layoutWebURLLL.getVisibility() == View.VISIBLE) {
            note.setWebLink(urlOfNoteET.getText().toString());
        }

        if (currentNote != null) {
            note.setId(currentNote.getId());
        }

        /*
        Save note with Room Database
         */
        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);

                finish();
            }
        }

        new SaveNoteTask().execute();
    }
}