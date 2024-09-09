package com.example.multitaskar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.multitaskar.Model.AudioModel;
import com.example.multitaskar.utility.AndroidUtitlity;

import java.util.ArrayList;
import java.util.Collections;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView Title_tv, currenttime_tv, totaltime_tv;
    SeekBar seekBar;
    ImageView previous_btn, next_btn, pauseplay_btn, Music_icon, back_btn, repeat_btn, shuffle_btn;

    ArrayList<AudioModel> songList;
    ArrayList<AudioModel> originalSongList;
    boolean RepeateMode;
    boolean ShuffleMode;
    AudioModel currentSong;
    String test_total_time, test_curr_time;

    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        Title_tv = findViewById(R.id.song_title);
        currenttime_tv = findViewById(R.id.current_time);
        totaltime_tv = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.SeekBar);
        previous_btn = findViewById(R.id.Previous_btn);
        next_btn = findViewById(R.id.Next_btn);
        pauseplay_btn = findViewById(R.id.pause_play);
        Music_icon = findViewById(R.id.music_icon_img);
        back_btn = findViewById(R.id.back_btn);
        shuffle_btn = findViewById(R.id.Shuffle_btn);
        repeat_btn = findViewById(R.id.Repeat_btn);

        back_btn.setOnClickListener(v -> onBackPressed());
        RepeateMode = false;
        ShuffleMode = false;

        shuffle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShuffleMode = !ShuffleMode;
                if (ShuffleMode) {
                    Collections.shuffle(songList);
                } else {
                    songList = new ArrayList<>(originalSongList);
                }
                AndroidUtitlity.showtoast(getApplicationContext(), "ShuffleMode " + ShuffleMode);
            }
        });

        repeat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepeateMode = !RepeateMode;
                AndroidUtitlity.showtoast(getApplicationContext(), "RepeatMode " + RepeateMode);
            }
        });

        songList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");
        originalSongList = new ArrayList<>(songList);

        setResoursewithMusic();
        Title_tv.setSelected(true);

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    test_curr_time = AndroidUtitlity.converttoMMSS(mediaPlayer.getCurrentPosition() + "");
                    currenttime_tv.setText(test_curr_time);

                    if (mediaPlayer.isPlaying()) {
                        pauseplay_btn.setImageResource(R.drawable.music_pause_btn);
                    } else {
                        pauseplay_btn.setImageResource(R.drawable.music_play_btn);
                    }
                }
                new Handler().postDelayed(this, 100);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (RepeateMode) {
                    PlayMusic();
                } else {
                    PlayNextSong();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void PlayMusic() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PlayNextSong() {
        if (MyMediaPlayer.CurrentIndex == songList.size() - 1) {
            if (RepeateMode) {
                MyMediaPlayer.CurrentIndex = 0;
            } else {
                return;
            }
        } else {
            MyMediaPlayer.CurrentIndex += 1;
        }
        mediaPlayer.reset();
        setResoursewithMusic();
    }

    private void PlayPreviousSong() {
        if (MyMediaPlayer.CurrentIndex == 0) {
            if (RepeateMode) {
                MyMediaPlayer.CurrentIndex = songList.size() - 1;
            } else {
                return;
            }
        } else {
            MyMediaPlayer.CurrentIndex -= 1;
        }
        mediaPlayer.reset();
        setResoursewithMusic();
    }

    private void PausePlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    void setResoursewithMusic() {
        currentSong = songList.get(MyMediaPlayer.CurrentIndex);
        Title_tv.setText(currentSong.getTitle());
        test_total_time = AndroidUtitlity.converttoMMSS(currentSong.getDuration());
        totaltime_tv.setText(test_total_time);

        pauseplay_btn.setOnClickListener(v -> PausePlay());
        next_btn.setOnClickListener(v -> PlayNextSong());
        previous_btn.setOnClickListener(v -> PlayPreviousSong());

        PlayMusic();
    }
}
