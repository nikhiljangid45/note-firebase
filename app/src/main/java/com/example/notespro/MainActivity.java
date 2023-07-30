package com.example.notespro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menyBtn;
    NoteAdapter noteAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recyler_view);
        menyBtn = findViewById(R.id.menu_btn);






        addNoteBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,NoteDetailsActivity.class)));
        menyBtn.setOnClickListener(v ->showMenu());
        setupRecycleView();


    }

  void showMenu(){
    PopupMenu popupMenu = new PopupMenu(MainActivity.this,menyBtn);
    popupMenu.getMenu().add("Logout");
    popupMenu.getMenu().add("Delete");
    popupMenu.show();
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(menuItem.getTitle()=="Logout"){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,loginActivity.class));
                finish();
                return true;
            }

            return false;

        }
    });


}

public void setupRecycleView() {

    Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
    FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note.class).build();
    recyclerView.setLayoutManager(new LinearLayoutManager(this));


    noteAdapter = new NoteAdapter(options, this);
    recyclerView.setAdapter(noteAdapter);
}

    @Override
    protected void onStart() {
        super.onStart();
          noteAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}