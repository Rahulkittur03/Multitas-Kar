package com.example.multitaskar.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multitaskar.MusicPlayerActivity;
import com.example.multitaskar.MyMediaPlayer;
import com.example.multitaskar.R;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    public MusicListAdapter(ArrayList<AudioModel> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    ArrayList<AudioModel> songList;
    Context context;
    boolean music_resume=false;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new MusicListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AudioModel songData=songList.get(position);
        holder.titleText_View.setText(songData.getTitle());

        if(MyMediaPlayer.CurrentIndex==position)
        {
            holder.titleText_View.setTextColor(Color.parseColor("#FF0000"));
            music_resume=true;
        }
        else {
            holder.titleText_View.setTextColor(Color.parseColor("#000000"));
            music_resume=false;
        }

        holder.itemView.setOnClickListener(v -> {
            //navigate  to another Activity
            MyMediaPlayer.getInstance().reset();
            MyMediaPlayer.CurrentIndex=position;
            Intent intent=new Intent(context, MusicPlayerActivity.class);
            intent.putExtra("LIST",songList);
            intent.putExtra("music_resume_onclick",music_resume);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView titleText_View;
        ImageView iconImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            titleText_View=itemView.findViewById(R.id.music_title_text);
            iconImageView=itemView.findViewById(R.id.icon_View_music);
        }
    }
}
