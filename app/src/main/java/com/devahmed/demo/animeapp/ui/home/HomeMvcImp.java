package com.devahmed.demo.animeapp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devahmed.demo.animeapp.Models.Image;
import com.devahmed.demo.animeapp.R;
import com.devahmed.demo.animeapp.common.BaseObservableMvcView;
import com.devahmed.demo.animeapp.ui.MainActivity;
import com.devahmed.demo.animeapp.ui.MobileImages.MobileMvc;
import com.devahmed.demo.animeapp.utils.RecyclerTouchListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeMvcImp extends BaseObservableMvcView<HomeMvc.Listener> implements HomeMvc {

    private RecyclerView homeRecycler;
    private List<Image> imageList;
    private HomeRecyclerAdapter adapter;
    private  FloatingActionButton addBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AVLoadingIndicatorView avi;
    public HomeMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.fragment_home , parent , false));


        swipeRefreshLayout = findViewById(R.id.swipRefresh);
        homeRecycler = findViewById(R.id.homeRecycler);
        avi = findViewById(R.id.aviMObile);
        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onAddBtnClicked();
                }
            }
        });
        imageList = new ArrayList<>();
        adapter = new HomeRecyclerAdapter(imageList);
        homeRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        homeRecycler.setHasFixedSize(true);
        homeRecycler.setAdapter(adapter);
        homeRecycler.addOnItemTouchListener(new RecyclerTouchListener(getContext(), homeRecycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                for(Listener listener : getmListeners()){
                    listener.onImageClicked(imageList.get(position));
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if(getContext().getResources().getString(R.string.isAdmin).equals("admin")){
                    for(Listener listener : getmListeners()){
                        listener.onImageLongClick(imageList.get(position));
                    }
                }else{

                }

            }
        }));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for(Listener listener : getmListeners()){
                    listener.onRefreshed();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        showAdminViews();
    }

    private void showAdminViews() {
        if(getContext().getResources().getString(R.string.isAdmin).equals("admin")){
            addBtn.show();
        }else{
            addBtn.hide();
        }
    }

    @Override
    public void showProgressBar() {
        avi.show();
    }

    @Override
    public void hideProgressBar() {
        avi.hide();
    }

    @Override
    public void bindHomeRecyclerData(List<Image> imagesList) {
        this.imageList = imagesList;
        adapter.setList(imagesList);
    }

    public  void showConfirmationMessage(final Image image){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Delete");
        builder.setMessage("Do you really want to delete this image ?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(Listener listener : getmListeners()){
                            listener.onChooseConfirmDelete(image);
                        }
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showOptionsDialog(String title , String [] options , final Image image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                // the user clicked on colors[which]
                switch (index){
                    case 0:
                        //edit
                        for(Listener listener : getmListeners()){
                            listener.onChooseEdite(image);
                        }
                        break;
                    case 1:
                        //delete
                        for(Listener listener : getmListeners()){
                            listener.onChooseDelete(image);
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    public void initBannerAd() {
        PublisherAdView mPublisherAdView;
        mPublisherAdView = findViewById(R.id.publisherAdViewHome);
        Random random = new Random();
        int i = random.nextInt(5);

        if(i == 10){
            PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
            mPublisherAdView.loadAd(adRequest);
        }

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
}

