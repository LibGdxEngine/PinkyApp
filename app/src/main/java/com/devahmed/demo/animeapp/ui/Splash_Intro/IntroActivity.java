package com.devahmed.demo.animeapp.ui.Splash_Intro;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.devahmed.demo.animeapp.R;
import com.devahmed.demo.animeapp.ui.MainActivity;


public class IntroActivity extends AppCompatActivity {
    ViewPagerAdapter viewPagerAdapter;
    ViewPager IntroViewPager;
    Button nextBtn , skipBtn;
    int counter  = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        setContentView(R.layout.activity_intro);

        nextBtn = findViewById(R.id.nextBtn);
        skipBtn = findViewById(R.id.skipBtn);
        IntroViewPager = findViewById(R.id.viewpager_intro);


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.AddFragment(new IntroFragment(R.drawable.intro1 , getResources().getString(R.string.INTRO_1)),"");
        viewPagerAdapter.AddFragment(new IntroFragment(R.drawable.intro2 , getResources().getString(R.string.INTRO_2)),"");
        viewPagerAdapter.AddFragment(new IntroFragment(R.drawable.intro6 , getResources().getString(R.string.INTRO_3)),"");

        IntroViewPager.setAdapter(viewPagerAdapter);

        IntroViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                counter = position;
                if(position == 2){
                    nextBtn.setText("Start Now");
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter == 2){
                    startActivity(new Intent(getBaseContext() , MainActivity.class));
                    finish();
                }
                IntroViewPager.setCurrentItem(++counter);

            }
        });


        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , MainActivity.class));
                finish();
            }
        });

    }
}
