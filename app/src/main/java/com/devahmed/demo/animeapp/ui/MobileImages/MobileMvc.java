package com.devahmed.demo.animeapp.ui.MobileImages;

import com.devahmed.demo.animeapp.Models.Image;

import java.util.List;

public interface MobileMvc {

    public interface Listener{
        void onRefresh();
        void onImageClicked(Image image);
        void onImageLongClick(Image image);
        void onChooseDelete(Image image);
        void onChooseEdite(Image image);
        void onChooseConfirmDelete(Image image);
    }

    public void bindData(List<Image> mobileImagesList);
    void showProgressBar();
    void hideProgressBar();
}
