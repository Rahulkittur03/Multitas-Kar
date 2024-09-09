package com.example.multitaskar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    CardView cardView1,cardView2,cardView3,cardView4,cardView5,cardView6;
    AdView adView;
    ImageView menu_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardView1=findViewById(R.id.Main_News_btn);
        cardView2=findViewById(R.id.Main_Reminder_btn);
        cardView3=findViewById(R.id.Main_Notes_btn);
        cardView4=findViewById(R.id.Main_Music_btn);
        cardView5=findViewById(R.id.Main_Meme_btn);
        cardView6=findViewById(R.id.Main_Logout_btn);
        menu_btn=findViewById(R.id.dropdown_menu);
        adView=findViewById(R.id.adView);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dropdown();
            }
        });
        //adContainerView.removeAllViews();
        //adContainerView.addView(adView);

        // Start loading the ad in the background.
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        cardView1.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),News_MainActivity.class);
            startActivity(intent);
        });
        cardView2.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),Reminder_MainActivity.class);
            startActivity(intent);
        });
        cardView3.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(), Notes_MainActivity.class);
            startActivity(intent);
        });
        cardView4.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),Music_MainActivity.class);
            startActivity(intent);
        });
        cardView5.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),Meme_MainActivity.class);
            startActivity(intent);
        });
        cardView6.setOnClickListener(v -> {

            Intent intent=new Intent(getApplicationContext(),JarvisMain_Activity.class);
            startActivity(intent);
            //finishAffinity();
        });
    }
    void Dropdown()
    {
        PopupMenu popupMenu=new PopupMenu(getApplicationContext(),menu_btn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle()=="Logout")
                {
                    FirebaseAuth auth=FirebaseAuth.getInstance();
                    auth.signOut();
                    Intent intent=new Intent(getApplicationContext(),login_activity.class);
                    startActivity(intent);
                    finishAffinity();
                }
                return false;
            }
        });
    }
}