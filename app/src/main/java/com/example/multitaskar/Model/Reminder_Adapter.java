package com.example.multitaskar.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multitaskar.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Reminder_Adapter extends FirestoreRecyclerAdapter<ReminderModel,Reminder_Adapter.reminderViewHolder> {
    Context context;
    public Reminder_Adapter(@NonNull FirestoreRecyclerOptions<ReminderModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull reminderViewHolder holder, int position, @NonNull ReminderModel reminder) {
    holder.titleTextView.setText(reminder.schedule_title);
    holder.ContentTextView.setText(reminder.schedule_Content);
    holder.DateTextView.setText(reminder.schedule_Date);
    holder.TimeTextView.setText(reminder.schedule_Time);

    }

    @NonNull
    @Override
    public reminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder,parent,false);
        return new reminderViewHolder(view);
    }

    class reminderViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,ContentTextView,DateTextView,TimeTextView;

        public reminderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.reminder_title_tv);
            ContentTextView=itemView.findViewById(R.id.reminder_content_tv);
            DateTextView=itemView.findViewById(R.id.reminder_Date);
            TimeTextView=itemView.findViewById(R.id.reminder_time);
        }
    }
}
