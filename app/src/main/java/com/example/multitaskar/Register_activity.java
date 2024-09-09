package com.example.multitaskar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Register_activity extends AppCompatActivity {

    EditText editText_email,editText_password,editText_c_passoword;
    Button Register_btn;
    ProgressBar progressbar;
    TextView Login_page;
    String email_str,passoword_str,passoword_c_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText_email=findViewById(R.id.email_register);
        editText_password=findViewById(R.id.new_password);
        editText_c_passoword=findViewById(R.id.new_confirm_password);
        progressbar=findViewById(R.id.progress_bar);
        Register_btn=findViewById(R.id.btn_register);
        Login_page=findViewById(R.id.login_Connect);

        progress_bar(false);
        Register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_str=editText_email.getText().toString();
                passoword_str=editText_password.getText().toString();
                passoword_c_str=editText_c_passoword.getText().toString();
                if(!isValid())
                {
                    return;
                }
                else {
                    Register_to_Firebase();
                }
            }
        });
        Login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),login_activity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
    public boolean isValid()
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(email_str).matches())
        {
            editText_email.setError("Invalid E-mail");
            return false;
        }
        if(passoword_str.length()<8)
        {
            editText_password.setError("password length is less then 8");
            return false;
        }
        if(passoword_c_str.length()<8)
        {
            editText_c_passoword.setError("password length is less then 8");
            return false;
        }
        if(!passoword_str.equals(passoword_c_str))
        {
            editText_c_passoword.setError("Password Does'nt match");
            return false;
        }
        return true;
    }
    public void progress_bar(boolean inprogress)
    {
        if(inprogress)
        {
            progressbar.setVisibility(View.VISIBLE);
            Register_btn.setVisibility(View.GONE);

        }
        else {
            progressbar.setVisibility(View.GONE);
            Register_btn.setVisibility(View.VISIBLE);
        }
    }
    public void Register_to_Firebase()
    {
        progress_bar(true);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email_str,passoword_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progress_bar(false);
                if(task.isSuccessful())
                {
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                    AndroidUtitlity.showtoast(getApplicationContext(),"Sucessfully Accout Created ,Check your Email to verify");
                    Intent intent=new Intent(getApplicationContext(),login_activity.class);
                    startActivity(intent);
                    finishAffinity();
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