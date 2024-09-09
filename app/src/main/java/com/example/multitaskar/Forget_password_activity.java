package com.example.multitaskar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.multitaskar.utility.AndroidUtitlity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_password_activity extends AppCompatActivity {

    EditText editText_email;
    Button btn_forgetpassword;
    ProgressBar progressBar;
    FirebaseAuth auth;
    String email_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        editText_email=findViewById(R.id.email_forget);
        btn_forgetpassword=findViewById(R.id.btn_forgetpassoword);
        progressBar=findViewById(R.id.progress_bar);
        progress_bar(false);

        btn_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              email_str = editText_email.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(editText_email.getText().toString()).matches()) {
                    editText_email.setError("Not a Valid Email");
                    return;
                }
              else
              {
                  ResetPassoword();
              }
            }
        });
    }
    public void progress_bar(boolean inprogress)
    {
        if(inprogress)
        {
            progressBar.setVisibility(View.VISIBLE);
            btn_forgetpassword.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            btn_forgetpassword.setVisibility(View.VISIBLE);
        }
    }
    void ResetPassoword()
    {
        progress_bar(true);
        auth=FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email_str).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progress_bar(false);
                if(task.isSuccessful())
                {
                    AndroidUtitlity.showtoast(getApplicationContext(),"Reset passoword link is been sent to your registered Email");
                    Intent intent=new Intent(getApplicationContext(),login_activity.class);
                    startActivity(intent);
                    finishAffinity();
                }
                else {
                    AndroidUtitlity.showtoast(getApplicationContext(),task.getException().toString());
                }
            }
        });
    }
}