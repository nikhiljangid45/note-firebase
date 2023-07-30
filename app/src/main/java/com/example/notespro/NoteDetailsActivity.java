package com.example.notespro;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;


public class NoteDetailsActivity extends AppCompatActivity {
EditText titleEditText,contentEditText;
ImageButton saveNoteBtn;
TextView pageTitleTextView;
String title,content,docId;
boolean isEditMode = false;
TextView deletNoteTextViewBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);


        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deletNoteTextViewBtn = findViewById(R.id.delet_note_text_view_btn);


        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId  = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode =true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);




        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            deletNoteTextViewBtn.setVisibility(View.VISIBLE);
        }
         deletNoteTextViewBtn.setOnClickListener(v ->deletNoteFromfirebase());
        saveNoteBtn.setOnClickListener(v ->saveNote());

    }
    private void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if(noteTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }

      Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        saveNoteToFirebase(note);

    }

    public  void saveNoteToFirebase(Note note){

        DocumentReference documentReference;

        if(isEditMode){
            // update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        }else {
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(NoteDetailsActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(NoteDetailsActivity.this, "failed is adding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void deletNoteFromfirebase() {

        DocumentReference documentReference;



        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(NoteDetailsActivity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(NoteDetailsActivity.this, "failed is adding", Toast.LENGTH_SHORT).show();
            }
        });
    }
}