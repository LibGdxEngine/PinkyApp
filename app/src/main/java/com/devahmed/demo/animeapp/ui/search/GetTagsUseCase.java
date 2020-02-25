package com.devahmed.demo.animeapp.ui.search;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.devahmed.demo.animeapp.Models.Tag;
import com.devahmed.demo.animeapp.common.BaseObservableMvcView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetTagsUseCase extends BaseObservableMvcView<GetTagsUseCase.Listener> {
    private final String FIREBASE_PATH = "Tags";
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<Tag> TagList;

    public interface Listener{
        void onTagSuccessfullyLoaded(List<Tag> TagList);
        void onTagFailedToLoad(DatabaseError error);
    }

    public GetTagsUseCase(FirebaseDatabase database) {
        this.database = database;
    }

    public void getTagsFromFirebase(){
        reference = database.getReference(FIREBASE_PATH);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TagList = new ArrayList<>();
                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
                    Tag model = postSnap.getValue(Tag.class);
                    TagList.add(model);
                }
//                Collections.shuffle(TagList);
                notifyChange(TagList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notifyCancelled(databaseError);
            }
        });

    }
    public void deleteTag(String TagID){
        reference = database.getReference(FIREBASE_PATH);
        reference.child(TagID).removeValue();
    }

    public void notifyChange(List<Tag> mProductsList){
        for(Listener listener : getmListeners()){
            listener.onTagSuccessfullyLoaded(mProductsList);
        }
    }
    public void notifyCancelled(DatabaseError error){
        for(Listener listener : getmListeners()){
            listener.onTagFailedToLoad(error);
        }
    }

}
