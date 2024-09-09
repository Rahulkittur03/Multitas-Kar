package com.example.multitaskar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.multitaskar.Model.NotesAdapter;
import com.example.multitaskar.Model.NotesModel;
import com.example.multitaskar.utility.AndroidUtitlity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import java.util.Queue;

public class Notes_MainActivity extends AppCompatActivity {
    FloatingActionButton addNoteBtn;
    RecyclerView recyclerViewNotes;
    NotesAdapter notesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);

        addNoteBtn=findViewById(R.id.add_new_note_btn);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),NotesDetailsActivity.class);
                startActivity(intent);
            }
        });
        recyclerViewNotes=findViewById(R.id.recycler_View_notes);
        setupRecyclerview();
    }

    void setupRecyclerview() {
        Query query= AndroidUtitlity.getCollectionReferenceNotes().orderBy("priority",Query.Direction.DESCENDING).orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NotesModel>options=new FirestoreRecyclerOptions.Builder<NotesModel>()
                .setQuery(query,NotesModel.class).build();
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter=new NotesAdapter(options,this);
        recyclerViewNotes.setAdapter(notesAdapter);
        AndroidUtitlity.getCollectionReferenceNotes().addSnapshotListener((value, error) -> {
            if (value != null) {
                notesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        notesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notesAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notesAdapter.notifyDataSetChanged();
    }
}