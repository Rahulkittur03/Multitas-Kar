package com.example.multitaskar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.multitaskar.Model.News_Recycler_Adapter;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class News_MainActivity  extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    List<Article> articleList=new ArrayList<>();
    LinearProgressIndicator progressIndicator;
    News_Recycler_Adapter adapter;
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);
        //ids
        recyclerView=findViewById(R.id.recycler_view);
        progressIndicator=findViewById(R.id.progress_bar);
        searchView=findViewById(R.id.search);
        btn1=findViewById(R.id.btn_1);
        btn2=findViewById(R.id.btn_2);
        btn3=findViewById(R.id.btn_3);
        btn4=findViewById(R.id.btn_4);
        btn5=findViewById(R.id.btn_5);
        btn6=findViewById(R.id.btn_6);
        btn7=findViewById(R.id.btn_7);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getnews("GENERAL",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        setRecyclerView();


        getnews("GENERAL",null);
    }

    void setRecyclerView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new News_Recycler_Adapter(articleList);
        recyclerView.setAdapter(adapter);
    }
    void changeprogress(boolean show)
    {
        if(show)
        {
            progressIndicator.setVisibility(View.VISIBLE);
        }
        else {
            progressIndicator.setVisibility(View.GONE);
        }
    }
    void getnews(String category,String query)
    {
        changeprogress(true);
        NewsApiClient newsApiClient = new NewsApiClient("5ca318b665f44082987bdf52e322c720");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .category(category)
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        response.getArticles().forEach(new Consumer<Article>() {
                            @Override
                            public void accept(Article article) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        changeprogress(false);
                                        articleList=response.getArticles();
                                        adapter.updatedata(articleList);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("Failed",""+throwable.getLocalizedMessage());
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        Button btn=(Button) view;
        String category=btn.getText().toString();
        getnews(category,null);
    }
}