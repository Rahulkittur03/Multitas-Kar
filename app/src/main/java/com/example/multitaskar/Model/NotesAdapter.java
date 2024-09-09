package com.example.multitaskar.Model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multitaskar.NotesDetailsActivity;
import com.example.multitaskar.R;
import com.example.multitaskar.utility.AndroidUtitlity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotesAdapter extends FirestoreRecyclerAdapter<NotesModel, NotesAdapter.NotesViewHolder> {
    Context context;
    public NotesAdapter(@NonNull FirestoreRecyclerOptions<NotesModel> options, Context context) {
        super(options);
        this.context=context;
    }
    @Override
    protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull NotesModel note) {
        holder.titletextView.setText(AndroidUtitlity.decryptData(note.title));
        Log.i("title date_itis",""+note.title);
        holder.ContentTextView.setText(AndroidUtitlity.decryptData(note.Content));
        holder.timestampTextview.setText(AndroidUtitlity.timestamptostring(note.timestamp));
        int Priority = note.Priority;
        switch (Priority) {
            case 1:
                holder.notes_priority.setBackgroundResource(R.drawable.green_shape);
                break;
            case 2:
                holder.notes_priority.setBackgroundResource(R.drawable.yellow_shape);
                break;
            case 3:
                holder.notes_priority.setBackgroundResource(R.drawable.red_shape);
                break;
            default:
                Log.i("color is not fetching", "error");
                holder.notes_priority.setBackgroundResource(R.drawable.red_shape);
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    NotesModel clickedNote = getItem(adapterPosition);

                    Intent intent = new Intent(context, NotesDetailsActivity.class);
                    intent.putExtra("title", AndroidUtitlity.decryptData(clickedNote.title));
                    intent.putExtra("content", AndroidUtitlity.decryptData(clickedNote.Content));
                    intent.putExtra("priority", clickedNote.Priority);
                    String docId = getSnapshots().getSnapshot(adapterPosition).getId();
                    intent.putExtra("docID", docId);
                    context.startActivity(intent);
                }
            }
        });
    }


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent,false);
        return new NotesViewHolder(view);
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView titletextView,ContentTextView,timestampTextview;
        View notes_priority;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            titletextView=itemView.findViewById(R.id.notes_title_tv);
            ContentTextView=itemView.findViewById(R.id.notes_content_tv);
            timestampTextview=itemView.findViewById(R.id.notes_timestamp_tv);
            notes_priority=itemView.findViewById(R.id.notes_priority);
        }
    }
}
