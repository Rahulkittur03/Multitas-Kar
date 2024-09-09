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
import android.widget.TextView;

import com.example.multitaskar.utility.AndroidUtitlity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity {
    EditText email_login_text,password_login_text;
    TextView register_page,Forgetpassoword;
    ProgressBar progressBar;
    Button btn_login;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email_login_text=findViewById(R.id.email_login);
        password_login_text=findViewById(R.id.login_password);
        register_page=findViewById(R.id.register_Connect);
        btn_login=findViewById(R.id.btn_login);
        progressBar=findViewById(R.id.progress_bar);
        Forgetpassoword=findViewById(R.id.Forget_password_text);
        progress_bar(false);

        Forgetpassoword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Forget_password_activity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=email_login_text.getText().toString();
                password=password_login_text.getText().toString();
                if(!validation_login())
                {
                    return;
                }
                else {
                    Login_account();
                }
            }
        });
        //Main Login activity

        //register_page
        register_page.setOnClickListener(v -> {
            Intent intent=new Intent(login_activity.this,Register_activity.class);
            startActivity(intent);
        });
    }
    public void progress_bar(boolean inprogress)
    {
        if(inprogress)
        {
            progressBar.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
        }
    }
    boolean validation_login()
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email_login_text.setError("Invalid E-mail");
            return false;
        }
        if(password.length()<8)
        {
            password_login_text.setError("password length is less then 8");
            return false;
        }
        return true;
    }
    void Login_account()
    {
        progress_bar(true);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress_bar(false);
                if(task.isSuccessful())
                {
                    if(firebaseAuth.getCurrentUser().isEmailVerified())
                    {
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                    else {
                        AndroidUtitlity.showtoast(getApplicationContext(),"Verify your Email");
                        return;
                    }
                }
                else
                {
                    AndroidUtitlity.showtoast(getApplicationContext(),task.getException().getLocalizedMessage());
                    return;
                }
            }
        });
    }
}