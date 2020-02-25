package com.devahmed.demo.animeapp.ui.AddImages;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.devahmed.demo.animeapp.Models.Image;
import com.devahmed.demo.animeapp.Models.Tag;
import com.devahmed.demo.animeapp.common.BaseObservableMvcView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddImageUseCase extends BaseObservableMvcView<AddImageUseCase.Listener> {
    private final Fragment context;
    private final String[] STORAGE_PERMISSION = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE" };
    public int GalleryREQUEST_CODE = 2;
    private static final int REQUEST_CODE_PERMISSIONS = 101;

    private final String IMAGES_PATH = "Images";
    private final String MOBILE_IMAGES_PATH = "MobileImages";
    private final String TAGS_PATH = "Tags";
    private final String FIRESTORAGE_PATH = "IMAGES";
    private String FIREBASE_PATH;
    public interface Listener {
        void onImageAddedSuccessfully();
        void onImageFailedToAdd();
        void onInputError(String message);
    }

    public AddImageUseCase(Fragment context) {
        this.context = context;
    }


    public void addNewImage(final Uri image  , final String tag , final String category){

        if(image == null){
            notifyInputError("الصورة غير صالحة");
            return;
        }

        if(!isValid(tag)){
            notifyInputError("تصنيف الصورة غير صالح");
            return;
        }

        if(category == null){
            notifyInputError("اختر نوع الصورة اولا !");
            return;
        }

        if(category.equals("صور عامة")){
            FIREBASE_PATH = IMAGES_PATH;
        }else{
            FIREBASE_PATH = MOBILE_IMAGES_PATH;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(FIRESTORAGE_PATH);
        final StorageReference imageFilePath = storageReference.child(image.getLastPathSegment());
        imageFilePath.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownloadLink = uri.toString();

                        Image myImage = new Image(imageDownloadLink);
                        myImage.setTag(tag);
                        //add post to firebase database
                        addPostToFirebase(myImage);
                        addTagToFirebase(myImage);
                        notifySuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //some thing goes wrong while uploading post
                        notifyFailure();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                notifyFailure();
            }
        });

    }


    public void addNewImage(final String imageURL  , final String tag , final String category){

        if(!isValid(imageURL)){
            notifyInputError("رابط الصورة غير صالح");
            return;
        }

        if(!isValid(tag)){
            notifyInputError("تصنيف الصورة غير صالح");
            return;
        }

        if(category == null){
            notifyInputError("اختر تصنيفا اولا");
            return;
        }

        if(category.equals("صور عامة")){
            FIREBASE_PATH = IMAGES_PATH;
        }else{
            FIREBASE_PATH = MOBILE_IMAGES_PATH;
        }
        Image image = new Image(imageURL);
        image.setTag(tag);
        //add post to firebase database
        addPostToFirebase(image);
        addTagToFirebase(image);
    }

    private void addTagToFirebase(Image image) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(TAGS_PATH);
        final Tag tag = new Tag(image.getTag() , 1);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Tag model = postSnap.getValue(Tag.class);
                    if(model.getTitle().equals(tag.getTitle())){
                        updateExistingTag(model);
                        return;
                    }
                }
                createNewTag(tag);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyFailure();
            }
        });

    }

    public void updateExistingImage(String imageURL , String imageTAG , String imageID , String category){
        if(!isValid(imageURL)){
            notifyInputError("رابط الصورة غير صالح");
            return;
        }

        if(!isValid(imageTAG)){
            notifyInputError("تصنيف الصورة غير صالح");
            return;
        }
        if(!isValid(imageID)){
            notifyInputError("ID is null");
            return;
        }

        if(category == null){
            notifyInputError("اختر تصنيفا اولا");
            return;
        }

        if(category.equals("صور عامة")){
            FIREBASE_PATH = IMAGES_PATH;
        }else{
            FIREBASE_PATH = MOBILE_IMAGES_PATH;
        }
        Image image = new Image(imageURL);
        image.setId(imageID);
        image.setTag(imageTAG);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FIREBASE_PATH);
        Map<String, Object> postValues = image.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(image.getId(), postValues);
        myRef.updateChildren(childUpdates);
        addTagToFirebase(image);
        notifySuccess();
    }

    private void createNewTag(Tag tag) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(TAGS_PATH).push();
        String key = myRef.getKey();
        tag.setId(key);
        myRef.setValue(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifySuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                notifyFailure();
            }
        });
    }

    private void updateExistingTag(Tag model) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(TAGS_PATH);
        model.setNoOfImages(model.getNoOfImages() + 1 );
        Map<String, Object> postValues = model.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(model.getId(), postValues);
        myRef.updateChildren(childUpdates);
    }


    private void addPostToFirebase(Image Image) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FIREBASE_PATH).push();
        //get post unique ID & update post key
        String key = myRef.getKey();
        Image.setId(key);
        //add post data to firebase database
        myRef.setValue(Image).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifySuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                notifyFailure();
            }
        });
    }
    public void openGallery() {
        if(StoragePermissionGranted()){
            //Open galleryIntent intent and wait for user to pick an image
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            context.startActivityForResult(galleryIntent, GalleryREQUEST_CODE);
        }else{
            askForStoragePermission();
            openGallery();
        }
    }
    private void askForStoragePermission(){
        ActivityCompat.requestPermissions(context.getActivity(), STORAGE_PERMISSION, REQUEST_CODE_PERMISSIONS);
    }

    private boolean StoragePermissionGranted(){

        if(ContextCompat.checkSelfPermission(context.getActivity(), STORAGE_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED){
            return false;
        }

        return true;
    }

    boolean isValid(String text){
        if(!text.isEmpty() || !text.trim().equals("")){
            if( (text.length() >= 3) ){
                return true;
            }
        }
        return false;
    }

    private void notifyFailure() {
        for(Listener listener : getmListeners()){
            listener.onImageFailedToAdd();
        }
    }

    private void notifySuccess(){
        for(Listener listener : getmListeners()){
            listener.onImageAddedSuccessfully();
        }
    }

    private void notifyInputError(String message){
        for(Listener listener : getmListeners()){
            listener.onInputError(message);
        }
    }
}
