package com.devahmed.demo.animeapp.ui.AddImages;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.devahmed.demo.animeapp.Models.Image;
import com.devahmed.demo.animeapp.Models.Tag;
import com.devahmed.demo.animeapp.R;
import com.devahmed.demo.animeapp.common.BaseObservableMvcView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddImageMvcImp extends BaseObservableMvcView<AddImageMvc.Listener> implements AddImageMvc {

    private FloatingActionButton actionButton;
    private EditText imageURL , Tag;
    ImageView clearURLBtnImageView , pickFromGallery;
    private RadioGroup radioGroup;
    private ChipGroup tagsGroup;
    private List<Tag> tagList;
    Switch aSwitch;
    ProgressBar progressBarAddImage;

    public AddImageMvcImp(LayoutInflater inflater , ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.fragment_add_image , parent , false));
        imageURL = findViewById(R.id.imageUrl);
        actionButton = findViewById(R.id.addImageBtn);
        progressBarAddImage = findViewById(R.id.progressBarAddImage);
        tagsGroup = findViewById(R.id.tagsGroupAdd);
        pickFromGallery = findViewById(R.id.pickFromGallery);

        aSwitch = findViewById(R.id.switchBtn);
//        aSwitch.setShowText(true);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aSwitch.setText("اختر من المعرض");
                }else{
                    aSwitch.setText("قم بوضع الرابط");
                }

                for(Listener listener : getmListeners()){
                    listener.onCheckBtnChanged(isChecked);
                }
            }
        });

        pickFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onPickFromGalleryClicked();
                }
            }
        });

        clearURLBtnImageView = findViewById(R.id.clearURL);
        clearURLBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageURL.setText("");
            }
        });

        tagsGroup.setSingleSelection(true);
        tagsGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if(chip != null){
                    Tag.setText(chip.getText().toString());
                }
            }
        });

        Tag = findViewById(R.id.tagtext);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Listener listener : getmListeners()){
                    listener.onAddBtnClicked();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && (checkedId > -1)) {
                    for(Listener listener : getmListeners()){
                        listener.onCategoryChoosed(rb.getText().toString());
                    }
                }
            }
        });
    }

    String getImageURl(){
        return imageURL.getText().toString();
    }

    String getTag(){
        return Tag.getText().toString();
    }

    @Override
    public void bindTagsData(List<Tag> tagList) {
        this.tagList = tagList;
        tagsGroup.removeAllViews();
        applyQuery(tagList);
    }

    @Override
    public void bindImageDataForEditing(Image image) {
        this.imageURL.setText(image.getImageUrl());
        this.Tag.setText(image.getTag());
    }

    @Override
    public void activateGalleryPickMode() {
        clearURLBtnImageView.setVisibility(View.GONE);
        imageURL.setVisibility(View.GONE);
        pickFromGallery.setVisibility(View.VISIBLE);
    }

    @Override
    public void activateURLMode() {
        clearURLBtnImageView.setVisibility(View.VISIBLE);
        imageURL.setVisibility(View.VISIBLE);
        pickFromGallery.setVisibility(View.GONE);
    }

    @Override
    public void bindPickedImage(Uri pickedImage) {
        pickFromGallery.setImageURI(pickedImage);
    }

    @Override
    public void showProgrssBar() {
        progressBarAddImage.setVisibility(View.VISIBLE);
        actionButton.hide();

    }

    @Override
    public void hideProgressBar() {
        progressBarAddImage.setVisibility(View.INVISIBLE);
        actionButton.show();
    }

    private void applyQuery(List<Tag> tagsList){
        for(int i = 0 ; i < tagsList.size(); i++){
            Chip chip = new Chip(getContext());
            chip.setId(i);
            chip.setTag(tagsList.get(i));
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setText(tagsList.get(i).getTitle());
            tagsGroup.addView(chip);
        }
    }
}
