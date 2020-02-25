package com.devahmed.demo.animeapp.ui.home;


import com.devahmed.demo.animeapp.Models.Image;

import java.util.List;

public interface HomeMvc {

    public interface Listener {
        void onImageClicked(Image image);
        void onAddBtnClicked();
        void onRefreshed();
        void onImageLongClick(Image image);
        void onChooseDelete(Image image);
        void onChooseEdite(Image image);
        void onChooseConfirmDelete(Image image);
    }

    void showProgressBar();
    void hideProgressBar();

    void bindHomeRecyclerData(List<Image> imagesList);
}

