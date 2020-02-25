package com.devahmed.demo.animeapp.ui.MobileImages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devahmed.demo.animeapp.Models.Image;
import com.devahmed.demo.animeapp.R;
import com.devahmed.demo.animeapp.common.BaseObservableMvcView;
import com.devahmed.demo.animeapp.utils.RecyclerTouchListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MobileMvcImp extends BaseObservableMvcView<MobileMvc.Listener> implements MobileMvc {

    private RecyclerView recyclerView;
    private MobileImagesAdapter adapter;
    private List<Image> imageList;
    private SwipeRefreshLayout refreshLayout;
    private AVLoadingIndicatorView avi;
    public MobileMvcImp(LayoutInflater inflater , ViewGroup group) {
        setRootView(inflater.inflate(R.layout.fragment_mobileimages, group , false));
        recyclerView = findViewById(R.id.mobileImagesRecycler);
        avi = findViewById(R.id.aviMObile);
        refreshLayout = findViewById(R.id.swipRefreshMobile);
        imageList = new ArrayList<>();
        adapter = new MobileImagesAdapter(imageList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext() , 2));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                for(Listener listener : getmListeners()){
                    listener.onImageClicked(imageList.get(position));
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                //if user is admin make long click available
                if(getContext().getResources().getString(R.string.isAdmin).equals("admin")){
                    for(Listener listener : getmListeners()){
                        listener.onImageLongClick(imageList.get(position));
                    }
                }else{
                    //do nothing
                }

            }
        }));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for(Listener listener : getmListeners()){
                    listener.onRefresh();
                }
                refreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void bindData(List<Image> mobileImagesList) {
        this.imageList = mobileImagesList;
        adapter.setList(imageList);
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

    @Override
    public void showProgressBar() {
        avi.show();
    }

    @Override
    public void hideProgressBar() {
        avi.hide();
    }

    public void showDeleteConfirmationMessage(final Image image){
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

    public void initBannerAd() {
        PublisherAdView mPublisherAdView;
        mPublisherAdView = findViewById(R.id.publisherAdViewMobile);

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
}
