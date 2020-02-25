package com.devahmed.demo.animeapp.ui.MobileImages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.devahmed.demo.animeapp.Models.Image;
import com.devahmed.demo.animeapp.R;
import com.devahmed.demo.animeapp.utils.Navigator;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MoblieSizesFragment extends Fragment implements MobileMvc.Listener, GetMobileImagesUseCase.Listener {

    private MobileMvcImp mvcImp;
    private GetMobileImagesUseCase mobileImagesUseCase;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mvcImp = new MobileMvcImp(getLayoutInflater() , null);
        mobileImagesUseCase = new GetMobileImagesUseCase(FirebaseDatabase.getInstance());
        mobileImagesUseCase.getImagesFromFirebase();
        return mvcImp.getRootView();
    }

    @Override
    public void onRefresh() {
        mobileImagesUseCase.getImagesFromFirebase();
    }

    @Override
    public void onImageClicked(Image image) {
        Bundle bundle = new Bundle();
        bundle.putString("imageURL" , image.getImageUrl());
        Navigator.instance(requireActivity()).navigate(R.id.fullscreenActivity , bundle);
    }

    @Override
    public void onImageLongClick(Image image) {
        String [] options = {"Edit "," Delete"};
        mvcImp.showOptionsDialog("Choose Option" , options , image );
    }

    @Override
    public void onChooseDelete(Image image) {
        mvcImp.showDeleteConfirmationMessage(image);
    }

    @Override
    public void onChooseEdite(Image image) {
        Bundle bundle = new Bundle();
        bundle.putString("FN", "EDIT");
        bundle.putString("imageID" , image.getId());
        bundle.putString("imageURL" , image.getImageUrl());
        bundle.putString("imageTAG" , image.getTag());
        Navigator.instance(requireActivity()).navigate(R.id.addImageFragment , bundle);
    }

    @Override
    public void onChooseConfirmDelete(Image image) {
        mobileImagesUseCase.deleteImage(image.getId() , image.getTag());
    }


    @Override
    public void onImageSuccessfullyLoaded(List<Image> imageList) {
        mvcImp.bindData(imageList);
        mvcImp.hideProgressBar();
    }

    @Override
    public void onImageFailedToLoad(DatabaseError error) {
        Toast.makeText(requireActivity(), "Failed To load \n internet problem", Toast.LENGTH_SHORT).show();
        mvcImp.hideProgressBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        mvcImp.initBannerAd();
        mobileImagesUseCase.registerListener(this);
        mvcImp.showProgressBar();
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        mobileImagesUseCase.unregisterListener(this);
        mvcImp.hideProgressBar();
    }
}