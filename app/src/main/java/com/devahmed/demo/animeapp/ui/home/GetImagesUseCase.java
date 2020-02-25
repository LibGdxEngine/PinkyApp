package com.devahmed.demo.animeapp.ui.home;

import androidx.annotation.NonNull;

import com.devahmed.demo.animeapp.Models.Image;
import com.devahmed.demo.animeapp.Models.Tag;
import com.devahmed.demo.animeapp.common.BaseObservableMvcView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetImagesUseCase extends BaseObservableMvcView<GetImagesUseCase.Listener> {

    private final String FIREBASE_PATH = "Images";
    private final String TAGS_PATH = "Tags";
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<Image> imageList;

    public interface Listener{
        void onImageSuccessfullyLoaded(List<Image> imageList);
        void onImageFailedToLoad(DatabaseError error);
    }

    public GetImagesUseCase(FirebaseDatabase database) {
        this.database = database;
    }

    public void getImagesFromFirebase(){
        reference = database.getReference(FIREBASE_PATH);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Image model = postSnap.getValue(Image.class);
                    imageList.add(model);
                    System.out.println("url is " + model.getImageUrl());
                }
                Collections.shuffle(imageList);
                notifyChange(imageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyCancelled(databaseError);
            }
        });

    }

    public void deleteImage(String imageID , String tag){
        reference = database.getReference(FIREBASE_PATH);
        reference.child(imageID).removeValue();
        updateTags(tag);
    }

    private void updateTags(String tag) {
        reference = database.getReference(TAGS_PATH);
        reference.orderByChild("title").equalTo(tag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Tag> tagList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Tag model = postSnap.getValue(Tag.class);
                    tagList.add(model);
                    if(model.getNoOfImages() == 1){
                        //the tag has no images and we should delete it .
                        deleteTag(model);
                    }else{
                        //the tag has many images and we should decrese it is number of images only .
                        updateExistingTag(model);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteTag(Tag model) {
        reference = database.getReference(TAGS_PATH);
        reference.child(model.getId()).removeValue();
    }

    private void updateExistingTag(Tag model) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(TAGS_PATH);
        model.setNoOfImages(model.getNoOfImages() - 1 );
        Map<String, Object> postValues = model.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(model.getId(), postValues);
        myRef.updateChildren(childUpdates);
    }

    public void notifyChange(List<Image> mProductsList){
        for(Listener listener : getmListeners()){
            listener.onImageSuccessfullyLoaded(mProductsList);
        }
    }
    public void notifyCancelled(DatabaseError error){
        for(Listener listener : getmListeners()){
            listener.onImageFailedToLoad(error);
        }
    }

}
