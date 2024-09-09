package com.example.multitaskar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.multitaskar.Model.NotesModel;
import com.example.multitaskar.utility.AndroidUtitlity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotesDetailsActivity extends AppCompatActivity {
    EditText title_tv,Content_tv;
    ImageView redcolor_btn,greencolor_btn,yellowcolor_btn,back_btn;
    FloatingActionButton SaveNotebtn,delete_note_btn;
    TextView page_tittle;
    String title,content,docId;
    boolean isEditMode=false;
    int Priority=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);

        title_tv=findViewById(R.id.title_edittext);
        Content_tv=findViewById(R.id.Content_Edittext);
        SaveNotebtn=findViewById(R.id.SaveNote_btn);
        page_tittle=findViewById(R.id.page_title);
        delete_note_btn=findViewById(R.id.Delete_note);

        redcolor_btn=findViewById(R.id.priority_red_color);
        greencolor_btn=findViewById(R.id.priority_green_color);
        yellowcolor_btn=findViewById(R.id.priority_yellow_color);
        back_btn=findViewById(R.id.back_btn);

        //reciveData
        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docID");
        Priority=getIntent().getIntExtra("prority",1);
        title_tv.setText(title);
        Content_tv.setText(content);
        if(docId!=null && !docId.isEmpty())
        {
            isEditMode=true;
        }
        if(isEditMode)
        {
            page_tittle.setText("Edit your Note");
            if (Priority==1) {
                greencolor_btn.setImageResource(R.drawable.done_img);
                redcolor_btn.setImageResource(0);
                yellowcolor_btn.setImageResource(0);
            } else if (Priority==2) {
                yellowcolor_btn.setImageResource(R.drawable.done_img);
                greencolor_btn.setImageResource(0);
                redcolor_btn.setImageResource(0);
            } else if (Priority==3) {
                redcolor_btn.setImageResource(R.drawable.done_img);
                greencolor_btn.setImageResource(0);
                yellowcolor_btn.setImageResource(0);
            }
            else {
                greencolor_btn.setImageResource(R.drawable.done_img);
                redcolor_btn.setImageResource(0);
                yellowcolor_btn.setImageResource(0);
            }
            delete_note_btn.setVisibility(View.VISIBLE);
        }

        delete_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_note_Firebase();
            }
        });
        back_btn.setOnClickListener(v -> onBackPressed());


        redcolor_btn.setOnClickListener(v -> {
            redcolor_btn.setImageResource(R.drawable.done_img);
            greencolor_btn.setImageResource(0);
            yellowcolor_btn.setImageResource(0);
            Priority=3;
        });

        greencolor_btn.setOnClickListener(v -> {
            greencolor_btn.setImageResource(R.drawable.done_img);
            redcolor_btn.setImageResource(0);
            yellowcolor_btn.setImageResource(0);
            Priority=1;
        });

        yellowcolor_btn.setOnClickListener(v -> {
            yellowcolor_btn.setImageResource(R.drawable.done_img);
            greencolor_btn.setImageResource(0);
            redcolor_btn.setImageResource(0);
            Priority=2;
        });

        SaveNotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }
    void saveNote()
    {
        String notetitle=title_tv.getText().toString();
        String noteContent=Content_tv.getText().toString();
        if(notetitle==null ||notetitle.isEmpty())
        {
            title_tv.setError("Title is Empty");
            return;
        }
        NotesModel notesModel=new NotesModel();
        notesModel.setTitle(AndroidUtitlity.encryptData(notetitle));
        notesModel.setPriority(Priority);
        notesModel.setContent(AndroidUtitlity.encryptData(noteContent));
        notesModel.setTimestamp(Timestamp.now());
        saveto_Firebase(notesModel);
    }

    public void saveto_Firebase(NotesModel notesModel)
    {
        DocumentReference documentReference;
        if(isEditMode)
        {
            documentReference=AndroidUtitlity.getCollectionReferenceNotes().document(docId);
        }
        else {
            documentReference=AndroidUtitlity.getCollectionReferenceNotes().document();
        }
        documentReference.set(notesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    AndroidUtitlity.showtoast(getApplicationContext(),"Note added Successfully");
                    finish();
                }
                else {
                    AndroidUtitlity.showtoast(getApplicationContext(),task.getException().getLocalizedMessage());
                }
            }
        });
    }
    void delete_note_Firebase()
    {
        DocumentReference documentReference;
        documentReference=AndroidUtitlity.getCollectionReferenceNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    AndroidUtitlity.showtoast(getApplicationContext(),"Note Deleted Successfully");
                    finish();
                }
                else {
                    AndroidUtitlity.showtoast(getApplicationContext(),task.getException().getLocalizedMessage());
                }
            }
        });
    }

}