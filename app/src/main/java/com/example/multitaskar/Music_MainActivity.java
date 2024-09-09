package com.example.multitaskar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.multitaskar.Model.AudioModel;
import com.example.multitaskar.Model.MusicListAdapter;
import com.example.multitaskar.utility.AndroidUtitlity;

import java.io.File;
import java.util.ArrayList;

public class Music_MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<AudioModel> songList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_main);

        recyclerView=findViewById(R.id.recycler_View);
        noMusicTextView=findViewById(R.id.No_song_found);

        if(checkPermission()==false)
        {
            requestPermission();
            return;
        }

        String[] projection=
                {
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DURATION
                };
        String selection=MediaStore.Audio.Media.IS_MUSIC+" !=0";
        Cursor cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);

        //Create a arraylist to store all the songs in it

        while(cursor.moveToNext())
        {
            AudioModel songData=new AudioModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            if(new File(songData.getPath()).exists()) {
                songList.add(songData);
            }
        }

        if(songList.size()==0)
        {
            noMusicTextView.setVisibility(View.VISIBLE);
        }
        else {
            //recycler View

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(songList,getApplicationContext()));
        }


    }

    boolean checkPermission()
    {
        int result= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result== PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    void requestPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(Music_MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            AndroidUtitlity.showtoast(getApplicationContext(),"READ PERMISSION IS REQUIRED ,PLEASE ALLOW FROM SETTING");
        }
        else {
            ActivityCompat.requestPermissions(Music_MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView!=null)
        {
            recyclerView.setAdapter(new MusicListAdapter(songList,getApplicationContext()));
        }
    }
}