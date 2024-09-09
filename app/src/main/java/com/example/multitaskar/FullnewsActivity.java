package com.example.multitaskar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class FullnewsActivity extends AppCompatActivity {

    WebView webView;
    ImageView back_btn;
    LinearProgressIndicator progressIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullnews);

        webView=findViewById(R.id.webview);
        progressIndicator=findViewById(R.id.progress_bar_i);

        progressIndicator.setVisibility(View.VISIBLE);
        back_btn=findViewById(R.id.back_btn);

        String url=getIntent().getStringExtra("url");
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressIndicator.setVisibility(View.GONE);
            }
        },1000);
        webView.setWebViewClient(new WebViewClient());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url);
            }
        });

        back_btn.setOnClickListener(v -> {
            onBackPressed();
        });

    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
        {
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}