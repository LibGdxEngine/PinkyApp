package com.devahmed.demo.animeapp.ui.AddImages;

import android.net.Uri;

import com.devahmed.demo.animeapp.Models.Image;
import com.devahmed.demo.animeapp.Models.Tag;

import java.util.List;

public interface AddImageMvc {

    interface Listener{
        void onAddBtnClicked();
        void onCategoryChoosed(String category);
        void onPickFromGalleryClicked();
        void onCheckBtnChanged(boolean isChecked);
    }

    void bindTagsData(List<Tag> tagList);
    void bindImageDataForEditing(Image image);
    void activateGalleryPickMode();
    void activateURLMode();
    void bindPickedImage(Uri pickedImage);
    void showProgrssBar();
    void hideProgressBar();
}
