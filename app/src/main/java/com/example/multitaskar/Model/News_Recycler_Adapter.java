package com.example.multitaskar.Model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multitaskar.R;
import com.example.multitaskar.FullnewsActivity;
import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class News_Recycler_Adapter extends  RecyclerView.Adapter<News_Recycler_Adapter.NewsViewHolderClass>{

    List<Article>articleList;
    public News_Recycler_Adapter(List<Article> articles)
    {
        this.articleList=articles;
    }

    @NonNull
    @Override
    public NewsViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row,parent,false);
        return new NewsViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolderClass holder, int position) {
        Article article=articleList.get(position);
        holder.title_text.setText(article.getTitle());
        holder.desc_text.setText(article.getSource().getName());
        Picasso.get().load(article.getUrlToImage()).error(R.drawable.no_img).placeholder(R.drawable.no_img).into(holder.news_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), FullnewsActivity.class);
                intent.putExtra("url",article.getUrl());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class NewsViewHolderClass extends RecyclerView.ViewHolder{

        TextView title_text,desc_text;
        ImageView news_img;
        public NewsViewHolderClass(@NonNull View itemView) {
            super(itemView);
            news_img=itemView.findViewById(R.id.artical_image);
            title_text=itemView.findViewById(R.id.artical_title);
            desc_text=itemView.findViewById(R.id.artical_desc);

        }
    }
    public void updatedata(List<Article> data)
    {
        articleList.clear();
        articleList.addAll(data);
    }
}
