package com.devahmed.demo.animeapp.ui.FullScreen;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class FullscreenActivity extends AppCompatActivity implements FullScreenActivityMvc.Listener, ImageUseCases.Listener {

    private FullScreenMvcImp mvcImp;
    private ImageUseCases  useCases;
    private String imageURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        mvcImp = new FullScreenMvcImp(getLayoutInflater() , null);
        useCases = new ImageUseCases(this);
        imageURL = getIntent().getExtras().getString("imageURL");
        mvcImp.bindImage(imageURL);

        setContentView(mvcImp.getRootView());
    }



    @Override
    protected void onStart(){
        super.onStart();
        mvcImp.registerListener(this);
        useCases.registerListener(this);
        mvcImp.initBannerAd();
        //init interstitial Add
        mvcImp.initInterstitialAd();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        useCases.unregisterListener(this);
    }

    @Override
    public void onBackPressed() {
        mvcImp.showInterstitialAd();
        super.onBackPressed();
    }


    @Override
    public void onDownloadBtnClicked() {
        if(useCases.isNetworkConnected()){
            if(useCases.StoragePermissionGranted()){
                //start to download the image if network is connected
                useCases.showDownloadDialogThenDownloadTheImage(imageURL);
            }else{
                useCases.askForStoragePermission();
            }
        }else{
            Toast.makeText(FullscreenActivity.this, "Network Error ! \n Can't download Image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSetBackgroundBtnClicked() {
        if(useCases.isNetworkConnected()){
            if(useCases.StoragePermissionGranted()){
                //start to download the image if network is connected
                useCases.showDownloadDialogThenSetImageAsBackground(imageURL);
            }else{
                useCases.askForStoragePermission();
            }
        }else{
            Toast.makeText(FullscreenActivity.this, "Network Error ! \n Can't download Image", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onShareBtnClicked() {
        if(useCases.isNetworkConnected()){
            if(useCases.StoragePermissionGranted()){
                //start to download the image if network is connected
                useCases.showDownloadDialogThenShareImage(imageURL);
            }else{
                useCases.askForStoragePermission();
            }
        }else{
            Toast.makeText(FullscreenActivity.this, "Network Error ! \n Can't download Image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onExitBtnClicked() {
        mvcImp.showInterstitialAd();
        finish();
    }

    @Override
    public void onImageDonwloaded() {
        mvcImp.showInterstitialAd();
    }

}
