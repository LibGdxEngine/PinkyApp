package com.devahmed.demo.animeapp.ui.Splash_Intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;

import com.devahmed.demo.animeapp.R;
import com.devahmed.demo.animeapp.ui.MainActivity;


public class SplashScreen extends AppCompatActivity {
    static int SECONDS = 2;
    Handler handler;
    boolean firstTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        firstTime = ! ( prefs.getBoolean("firstTime", false) );

        if (firstTime) {
            // <---- run your one time code here
            // mark first time has ran.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firstTime){
                    //if first time go to intro slides activity
                    Intent intent = new Intent(SplashScreen.this , IntroActivity.class);
                    startActivity(intent);
                }else{
                    //if not first time go to home directly
                    Intent intent = new Intent(SplashScreen.this , MainActivity.class);
                    startActivity(intent);
                }
                finish();

            }

        },SECONDS * 1000);
    }
}
