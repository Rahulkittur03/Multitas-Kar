package com.example.multitaskar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multitaskar.Model.AudioModel;
import com.example.multitaskar.Model.MusicListAdapter;
import com.example.multitaskar.utility.AndroidUtitlity;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JarvisMain_Activity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    TextView text_speach;
    FloatingActionButton record_btn;
    TextToSpeech textToSpeech;
    ProgressBar progressBar;
    public static final MediaType JSON = MediaType.get("application/json");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jarvis_main);
        text_speach = findViewById(R.id.speach_text);
        record_btn = findViewById(R.id.Record_btn);
        progressBar=findViewById(R.id.progress_bar_jarvis);
        progressBar.setVisibility(View.GONE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Dexter.withContext(this)
                        .withPermission(
                                Manifest.permission.RECORD_AUDIO
                        ).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        System.exit(0);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                });
        InitializeTextToSpeak();
        record_btn.setOnClickListener(v ->
                recording()
        );
    }

    void InitializeTextToSpeak() {
        progressBar.setVisibility(View.VISIBLE);
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(textToSpeech.getEngines().size()==0)
                {
                    AndroidUtitlity.showtoast(getApplicationContext(),"Engine is not available");
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    speak("Hi i am Jarvis"+AndroidUtitlity.timeNow());
                }
            }
        });
    }

    void speak(String msg)
    {
        textToSpeech.speak(msg,TextToSpeech.QUEUE_FLUSH,null,null);
    }
//    boolean checkPermission()
//    {
//        int result= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
//        if(result== PackageManager.PERMISSION_GRANTED)
//        {
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }
//    void requestPermission()
//    {
//        if(ActivityCompat.shouldShowRequestPermissionRationale(JarvisMain_Activity.this, Manifest.permission.RECORD_AUDIO))
//        {
//            AndroidUtitlity.showtoast(getApplicationContext(),"RECORD AUDIO IS REQUIRED ,PLEASE ALLOW FROM SETTING");
//        }
//        else {
//            ActivityCompat.requestPermissions(JarvisMain_Activity.this,new String[]{Manifest.permission.RECORD_AUDIO},1234);
//        }
//    }
    void recording() {

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            try {
                startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);
            }
            catch ( Exception e)
            {
                Log.i("Exception",""+e.getLocalizedMessage());
            }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK && null!=data)
                {
                    ArrayList<String>result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //Remove before production
                    text_speach.setText("" + result.get(0));
                    response(result.get(0));
                }
                break;
            }
        }
    }

    void response(String s) {
        String msg=s.toLowerCase();
        if(msg.contains("search"))
        {
            CallAPI(msg);
            return;
        }
        if(msg.indexOf("not fine")!=-1)
        {
            speak("take care sir!");
            return;
        }else if(msg.indexOf("fine")!=-1)
        {
            speak("its good to know that you are fine");
            return;
        }
        if(msg.indexOf("what")!=-1)
        {
            if(msg.indexOf("your")!=-1)
            {
                if(msg.indexOf("name")!=-1)
                {
                    speak("Jarvis");
                }
            }
            if(msg.indexOf("time")!=-1)
            {
                    Calendar calendar = Calendar.getInstance();
                    Date date = calendar.getTime();

                    // Format the time in 12-hour format
                    String timenow = DateUtils.formatDateTime(getApplicationContext(), date.getTime(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_12HOUR);

                    // Speak the current time
                    speak("The current time is " + timenow);
                    return;
            }
            if(msg.indexOf("today")!=-1)
            {
                if(msg.indexOf("date")!=-1)
                {
                    LocalDate localDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
                    String todays_date = localDate.format(formatter);
                    speak("The current date is " + todays_date);
                    return;
                }
            }
        }
        if(msg.contains("what"))
        {
            if(msg.contains("can")) {
                if (msg.contains("do")) {
                    String capabilities = "I can help you with various tasks such as:\n" +
                            "- Searching the web\n" +
                            "- Telling the current time and date\n" +
                            "- Playing music on YouTube\n" +
                            "- Opening apps like Google, Chrome, YouTube, Facebook and Instagram\n" +
                            "- Managing your notes, music, reminders, news, and memes";
                    speak(capabilities);
                    text_speach.setText(capabilities);
                    return;
                }
            }
        }
        if(msg.indexOf("open")!=-1)
        {
            if(msg.indexOf("google")!=-1)
            {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"));
                startActivity(intent); 
                return;
            }
            if(msg.indexOf("browser")!=-1)
            {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"));
                startActivity(intent);
                return;
            }
            if(msg.indexOf("chrome")!=-1)
            {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"));
                startActivity(intent);
                return;
            }
            if(msg.indexOf("youtube")!=-1)
            {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
                startActivity(intent);
                return;
            }
            if(msg.indexOf("facebook")!=-1)
            {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                startActivity(intent);
                return;
            }
            if(msg.indexOf("instagram")!=-1)
            {
                Uri uri = Uri.parse("http://instagram.com/");
                Intent instaIntent = new Intent(Intent.ACTION_VIEW, uri);
                instaIntent.setPackage("com.instagram.android");
                try {
                    startActivity(instaIntent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/")));
                }
                return;

            }
            if(msg.indexOf("notes")!=-1 ||msg.indexOf("note")!=-1)
            {
                Intent intent=new Intent(getApplicationContext(),Notes_MainActivity.class);
                startActivity(intent);
            }
            if(msg.indexOf("music")!=-1)
            {
                Intent intent=new Intent(getApplicationContext(),Music_MainActivity.class);
                startActivity(intent);
            }
            if(msg.indexOf("reminder")!=-1)
            {
                Intent intent=new Intent(getApplicationContext(),Reminder_MainActivity.class);
                startActivity(intent);
            }
            if(msg.indexOf("news")!=-1)
            {
                Intent intent=new Intent(getApplicationContext(),News_MainActivity.class);
                startActivity(intent);
            }
            if(msg.indexOf("meme")!=-1)
            {
                Intent intent=new Intent(getApplicationContext(),Meme_MainActivity.class);
                startActivity(intent);
            }
            //Whatapp Open
            if(msg.indexOf("whatsapp")!=-1)
            {
                Context context = this;
                Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                if (intent != null) {
                    context.startActivity(intent);
                } else {
                    // Handle the case where WhatsApp is not installed or launch intent is not available
                    Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
                }
            }
            return;
        }
        if (msg.contains("play")) {
            String query= msg.replace("play","").replace("music","").trim();
            Log.i("music","this is name"+query);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=" + Uri.encode(query)));
            startActivity(intent);
        }
        if(msg.contains("hi"))
        {
            speak("Hello Sir! How are you ?");
            return;
        }
        if(msg.contains("hello")) {
            if (msg.contains("how")) {
                speak("i am fine how are you!");
                }
            return;
        }
    }

    void CallAPI(String msg)
    {
        progressBar.setVisibility(View.VISIBLE);
        GenerativeModel gm = new GenerativeModel( "gemini-1.5-flash","AIzaSyAkJX9tayicN0NjngZi0fzSddGbSG1Mlvk");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(msg.substring(6))
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                progressBar.setVisibility(View.INVISIBLE);
                String resultText = result.getText();
                if(resultText.length()>150)
                {
                    speak(resultText.substring(0,150)+", and So On.");

                }
                else {
                    speak(resultText);
                }
                text_speach.setText(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                // Handle errors more specifically (e.g., network issues, model errors)
                progressBar.setVisibility(View.INVISIBLE);
                text_speach.setText("Error generating content " +t.getMessage());
                Log.i("Server", "Error generating content: " + t.getMessage().trim());
                }
            }, this.getMainExecutor());
        }
        else
        {
            Log.i("VersionWarning", "Android version below P might have compatibility issues.");
        }
    }
}
