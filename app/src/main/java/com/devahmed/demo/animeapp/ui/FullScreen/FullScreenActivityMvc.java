package com.devahmed.demo.animeapp.ui.FullScreen;

public interface FullScreenActivityMvc {

    interface Listener{
        void onDownloadBtnClicked();
        void onSetBackgroundBtnClicked();
        void onShareBtnClicked();
        void onExitBtnClicked();
    }
    void bindImage(String imageURL);
    void initInterstitialAd();
    void showInterstitialAd();
    void initBannerAd();


}
