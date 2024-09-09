package com.example.multitaskar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.multitaskar.Model.ReminderModel;
import com.example.multitaskar.Model.Reminder_Adapter;
import com.example.multitaskar.utility.AndroidUtitlity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Reminder_MainActivity extends AppCompatActivity {
    FloatingActionButton add_reminder_btn;
    RecyclerView recyclerView;
    Reminder_Adapter reminderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_main);
        add_reminder_btn=findViewById(R.id.add_new_reminder_btn);
        recyclerView=findViewById(R.id.recycler_View_reminder);


        add_reminder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), add_new_Reminder.class);
                startActivity(intent);
            }
        });
        setupRecyclerView();
    }
    void setupRecyclerView()
    {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(now.getTime());
        Query query = AndroidUtitlity.getCollectionReferenceReminder()
                .orderBy("schedule_Date", Query.Direction.ASCENDING)
                .orderBy("schedule_Time", Query.Direction.ASCENDING)
                .whereEqualTo("schedule_status", "1")
                .whereGreaterThanOrEqualTo("schedule_Date", currentDate);

        // Configure FirestoreRecyclerOptions
        FirestoreRecyclerOptions<ReminderModel> options = new FirestoreRecyclerOptions.Builder<ReminderModel>()
                .setQuery(query, ReminderModel.class)
                .build();

        // Set up the RecyclerView with a LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with the options and set it to the RecyclerView
        reminderAdapter = new Reminder_Adapter(options, this);
        recyclerView.setAdapter(reminderAdapter);
        AndroidUtitlity.getCollectionReferenceReminder().addSnapshotListener((value, error) -> {
            if (value != null) {
                reminderAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        reminderAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        reminderAdapter.stopListening();
    }
}