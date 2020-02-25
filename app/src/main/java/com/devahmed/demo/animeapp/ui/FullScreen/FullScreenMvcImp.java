package com.devahmed.demo.animeapp.ui.FullScreen;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devahmed.demo.animeapp.R;
import com.devahmed.demo.animeapp.common.BaseObservableMvcView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.wang.avi.AVLoadingIndicatorView;

public class FullScreenMvcImp extends BaseObservableMvcView<FullScreenActivityMvc.Listener> implements FullScreenActivityMvc{

    ImageView imageView;
    LinearLayout ShareBtnLinearLayout;
    Button downloadImageBtnButton , setBackgroundBtn;
    AVLoadingIndicatorView loadingIndicatorView;
    ImageView closeBtnImageView1;
    private PublisherInterstitialAd mPublisherInterstitialAd;
    PublisherAdView mPublisherAdView;
    public FullScreenMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.activity_fullscreen , parent , false));
        initViews();
        loadingIndicatorView.show();


    }

    private void initViews() {

        mPublisherAdView = findViewById(R.id.publisherAdViewFull);
        imageView = findViewById(R.id.fullImageViewFullScreen);
        ShareBtnLinearLayout = findViewById(R.id.shareBtn);
        downloadImageBtnButton = findViewById(R.id.download);
        setBackgroundBtn = findViewById(R.id.setBackground);
        closeBtnImageView1 = findViewById(R.id.closeBtn);
        loadingIndicatorView = findViewById(R.id.avi);
        ShareBtnLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onShareBtnClicked();
                }
            }
        });
        closeBtnImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onExitBtnClicked();
                }
            }
        });
        downloadImageBtnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onDownloadBtnClicked();
                }
            }
        });
        setBackgroundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onSetBackgroundBtnClicked();
                }
            }
        });

    }

    @Override
    public void bindImage(String imageURL) {
        Glide.with(getContext())
                .load(imageURL)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        loadingIndicatorView.hide();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loadingIndicatorView.hide();
                        return false;
                    }
                })
                .placeholder(R.color.black_overlay)
                .transition(DrawableTransitionOptions.withCrossFade(100))
                .into(imageView);
    }

    @Override
    public void initInterstitialAd() {
        mPublisherInterstitialAd = new PublisherInterstitialAd(getContext());
        mPublisherInterstitialAd.setAdUnitId(getContext().getResources().getString(R.string.AdsAppIntestitial));
        mPublisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());
    }

    @Override
    public void showInterstitialAd() {
        if (mPublisherInterstitialAd.isLoaded()) {
            mPublisherInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    @Override
    public void initBannerAd() {
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
