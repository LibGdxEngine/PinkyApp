package com.devahmed.demo.animeapp.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.devahmed.demo.animeapp.Models.Tag;
import com.devahmed.demo.animeapp.R;
import com.devahmed.demo.animeapp.utils.Navigator;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchMvc.Listener, GetTagsUseCase.Listener {

    private SearchMvcImp mvcImp;
    private GetTagsUseCase getTagsUseCase;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mvcImp = new SearchMvcImp(getLayoutInflater() , null);
        getTagsUseCase = new GetTagsUseCase(FirebaseDatabase.getInstance());
        getTagsUseCase.getTagsFromFirebase();


        return mvcImp.getRootView();
    }

    @Override
    public void onQueryChange(String text) {
        mvcImp.filterTags(text);
    }

    @Override
    public void onTagClicked(String tag) {
        Bundle bundle = new Bundle();
        bundle.putString("tag" , tag);
        Navigator.instance(requireActivity()).navigate(R.id.searchResultActivity , bundle);
    }


    @Override
    public void onSearchViewFocusChanged(View view, boolean b) {
        System.out.println("Focus changed");
    }


    @Override
    public void onStart() {
        super.onStart();
        mvcImp.registerListener(this);
        getTagsUseCase.registerListener(this);
        mvcImp.showProgressBar();
    }

    @Override
    public void onStop() {
        super.onStop();
        mvcImp.unregisterListener(this);
        getTagsUseCase.unregisterListener(this);
        mvcImp.hideProgessBar();
    }

    @Override
    public void onTagSuccessfullyLoaded(List<Tag> TagList) {
        mvcImp.bindTagsData(TagList);
        mvcImp.hideProgessBar();
    }

    @Override
    public void onTagFailedToLoad(DatabaseError error) {
        Toast.makeText(requireActivity(), "Tags can't be loaded " + error.getMessage(), Toast.LENGTH_SHORT).show();
        mvcImp.hideProgessBar();
    }
}