package com.devahmed.demo.animeapp.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.devahmed.demo.animeapp.Models.Image;
import com.devahmed.demo.animeapp.R;
import com.devahmed.demo.animeapp.utils.Navigator;
import com.devahmed.demo.animeapp.utils.RecyclerTouchListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchResultAdapter adapter;
    List<Image> imageList;
    FirebaseDatabase database;
    DatabaseReference ImagesReference , MobileImagesReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        recyclerView = findViewById(R.id.resultRecycler);
        imageList = new ArrayList<>();
        adapter = new SearchResultAdapter(imageList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2 , LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        ImagesReference = database.getReference("Images");
        MobileImagesReference = database.getReference("MobileImages");
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("imageURL" , imageList.get(position).getImageUrl());
                Navigator.instance(SearchResultActivity.this).navigate(R.id.fullscreenActivity , bundle);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
    public void initBannerAd() {
        PublisherAdView mPublisherAdView;
        mPublisherAdView = findViewById(R.id.publisherAdViewSearchResult);

        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);

//        mPublisherAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                Toast.makeText(getContext(), "Loaded", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//                Toast.makeText(getContext(), "Failed " + errorCode, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//                Toast.makeText(getContext(), "Opened", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//                Toast.makeText(getContext(), "LeftApp", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//                Toast.makeText(getContext(), "Closed", Toast.LENGTH_SHORT).show();
//            }
//        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        initBannerAd();
        imageList = new ArrayList<>();
        String tag = getIntent().getExtras().getString("tag");
        ImagesReference.orderByChild("tag").equalTo(tag).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Image model = postSnap.getValue(Image.class);
                    imageList.add(model);
                }
                Collections.shuffle(imageList);
                adapter.setList(imageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        MobileImagesReference.orderByChild("tag").equalTo(tag).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Image model = postSnap.getValue(Image.class);
                    imageList.add(model);
                }
                Collections.shuffle(imageList);
                adapter.setList(imageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
