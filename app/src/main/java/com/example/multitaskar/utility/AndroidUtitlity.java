package com.example.multitaskar.utility;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AndroidUtitlity {
    public static void showtoast(Context context,String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static  String converttoMMSS(String duration)
    {
        Long millis=Long.parseLong(duration);
       return String.format("%02d:%02d",
               TimeUnit.MILLISECONDS.toMinutes(millis)%TimeUnit.HOURS.toMinutes(1)
               , TimeUnit.MILLISECONDS.toSeconds(millis)%TimeUnit.MINUTES.toSeconds(1));
    }
    public static CollectionReference getCollectionReferenceNotes()
    {
        FirebaseUser currentuser= FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("users").document(currentuser.getUid()).collection("my_notes");
    }
    public static String timestamptostring(Timestamp timestamp)
    {
        return new SimpleDateFormat("dd/MM/yyyy").format(timestamp.toDate());
    }
    public static String timeNow()
    {
        String s="";
        Calendar c=Calendar.getInstance();
        int time=c.get(Calendar.HOUR_OF_DAY);
        if(time>=0&& time<=12)
        {
            s="Good Morning!";
        }
        else if(time>=12&&time<=16)
        {
            s="Good Afternoon!";
        }
        else if(time>=16&&time<=0)
        {
            s="Good Evening!";
        }
        else
        {
            s="";
        }
        return s;
    }



    public static CollectionReference getCollectionReferenceReminder()
    {
        FirebaseUser currentuser= FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("users").document(currentuser.getUid()).collection("my_schedule");
    }
    //AtbashCipher
    public static String encryptData(String data) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : data.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                char encryptedChar = (char) (25 - (c - base) + base);
                encrypted.append(encryptedChar);
            } else {
                encrypted.append(c); // Non-alphabetic characters remain unchanged
            }
        }
        return encrypted.toString();
    }
    public static String decryptData(String data) {
        return encryptData(data); // Decrypting an Atbash cipher encrypted text gives the original text
    }
}
