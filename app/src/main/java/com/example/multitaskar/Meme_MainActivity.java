package com.example.multitaskar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.chooser.ChooserAction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.multitaskar.utility.AndroidUtitlity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Meme_MainActivity extends AppCompatActivity {

    Button load_meme_btn,share_meme_btn;
    ImageView meme_imageView;
    ProgressBar progressBar;
    String imageurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_main);

        meme_imageView=findViewById(R.id.Meme_imageView);
        load_meme_btn=findViewById(R.id.load_meme_btn);
        share_meme_btn=findViewById(R.id.share_meme_btn);
        progressBar=findViewById(R.id.progress_bar_meme);

        load_meme_btn.setOnClickListener(v -> loadMeme());
        share_meme_btn.setOnClickListener(v -> shareMeme());

        loadMeme();

    }
    void loadMeme()
    {
        progressBar.setVisibility(View.VISIBLE);
        //meme_imageView.setVisibility(View.GONE);
        String url = "https://meme-api.com/gimme";

        RequestQueue queue= Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            imageurl=response.getString("url");
                            Glide.with(getApplicationContext()).addDefaultRequestListener(new RequestListener<Object>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Object> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(@NonNull Object resource, @NonNull Object model, Target<Object> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    meme_imageView.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            }).load(imageurl).into(meme_imageView);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        AndroidUtitlity.showtoast(getApplicationContext(),error.getMessage());
                    }
                });

        queue.add(jsonObjectRequest);
    }

    void shareMeme()
    {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey,Checkout this new MEME "+imageurl);

        Intent chooserAction=Intent.createChooser(intent,"Share this meme using ...");
        startActivity(chooserAction);
    }
}