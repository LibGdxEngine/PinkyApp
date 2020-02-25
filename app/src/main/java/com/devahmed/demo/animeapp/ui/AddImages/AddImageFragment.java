package com.devahmed.demo.animeapp.ui.AddImages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devahmed.demo.animeapp.Models.Image;
import com.devahmed.demo.animeapp.Models.Tag;
import com.devahmed.demo.animeapp.ui.search.GetTagsUseCase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddImageFragment extends Fragment implements AddImageMvc.Listener, AddImageUseCase.Listener, GetTagsUseCase.Listener {

    AddImageMvcImp  mvcImp;
    AddImageUseCase addImageUseCase;
    GetTagsUseCase getTagsUseCase;
    String category;
    String fn;
    Image editedImage;
    String imageID;
    Uri pickedImage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mvcImp = new AddImageMvcImp(getLayoutInflater() , null);
        addImageUseCase = new AddImageUseCase(this);
        getTagsUseCase = new GetTagsUseCase(FirebaseDatabase.getInstance());
        getTagsUseCase.getTagsFromFirebase();

        fn = getArguments().getString("FN");
        if(fn.equals("EDIT")){
            String imageURL = getArguments().getString("imageURL");
            imageID = getArguments().getString("imageID");
            String imageTAG = getArguments().getString("imageTAG");
            editedImage = new Image(imageURL);
            editedImage.setTag(imageTAG);
            editedImage.setId(imageID);
            mvcImp.bindImageDataForEditing(editedImage);
        }

        return mvcImp.getRootView();
    }


    @Override
    public void onAddBtnClicked() {
        mvcImp.showProgrssBar();
        if(fn.equals("EDIT")){
            addImageUseCase.updateExistingImage(mvcImp.getImageURl() ,mvcImp.getTag() , imageID ,  category);
        }else{
            if(pickedImage != null){
                //add image from gallery to firebase
                addImageUseCase.addNewImage(pickedImage , mvcImp.getTag() , category);
            }else{
                //add image from URL link
                addImageUseCase.addNewImage(mvcImp.getImageURl() , mvcImp.getTag() , category);
            }
        }
    }

    @Override
    public void onCategoryChoosed(String category) {
        this.category = category;
    }

    @Override
    public void onPickFromGalleryClicked() {
        addImageUseCase.openGallery();
    }

    @Override
    public void onCheckBtnChanged(boolean isChecked) {
        if(isChecked){
            mvcImp.activateGalleryPickMode();
        }else{
            mvcImp.activateURLMode();
        }
    }

    @Override
    public void onImageAddedSuccessfully() {
        Toast.makeText(requireContext(), "تم اضافة الصورة بنجاح", Toast.LENGTH_SHORT).show();
        mvcImp.hideProgressBar();
    }

    @Override
    public void onImageFailedToAdd() {
        Toast.makeText(requireContext(), "خطأ ! لم تتم اضافة الصورة", Toast.LENGTH_SHORT).show();
        mvcImp.hideProgressBar();
    }

    @Override
    public void onInputError(String message) {
        Toast.makeText(requireContext(), "" + message, Toast.LENGTH_SHORT).show();
        mvcImp.hideProgressBar();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == addImageUseCase.GalleryREQUEST_CODE && data != null){
            // The user successfully picked an image
            System.out.println("Here");
            pickedImage = data.getData();
        }
        mvcImp.bindPickedImage(pickedImage);
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        addImageUseCase.registerListener(this);
        getTagsUseCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        addImageUseCase.unregisterListener(this);
        getTagsUseCase.unregisterListener(this);
        mvcImp.hideProgressBar();
    }

    @Override
    public void onTagSuccessfullyLoaded(List<Tag> TagList) {
        mvcImp.bindTagsData(TagList);
    }

    @Override
    public void onTagFailedToLoad(DatabaseError error) {
        Toast.makeText(requireActivity(), "Failed to load tags", Toast.LENGTH_SHORT).show();
    }
}
