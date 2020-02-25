package com.devahmed.demo.animeapp.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.devahmed.demo.animeapp.Models.Tag;
import com.devahmed.demo.animeapp.R;
import com.devahmed.demo.animeapp.common.BaseObservableMvcView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class SearchMvcImp extends BaseObservableMvcView<SearchMvc.Listener> implements SearchMvc {

    SearchView searchView;
    ChipGroup tagsGroup;
    List<Tag> tagList , searchList;
    AVLoadingIndicatorView avl;
    public SearchMvcImp(LayoutInflater inflater , ViewGroup group) {
        setRootView(inflater.inflate(R.layout.fragment_search , group , false));
        tagsGroup = findViewById(R.id.tagsGroupAdd);
        avl = findViewById(R.id.aviMObile);
        tagsGroup.setSingleSelection(true);
        tagsGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if(chip != null){
                    for(Listener listener : getmListeners()){
                        listener.onTagClicked(chip.getText().toString());
                    }
                }
            }
        });
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View view, boolean b) {
                // Do your magic here
                for(Listener listener : getmListeners()){
                    listener.onSearchViewFocusChanged(view , b);
                }
            }

        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(Listener listener : getmListeners()){
                    listener.onQueryChange(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                for(Listener listener : getmListeners()){
                    listener.onQueryChange(newText);
                }
                return false;
            }
        });
    }

    @Override
    public void bindTagsData(List<Tag> tagsList) {
        this.tagList = new ArrayList<>();
        this.tagList = tagsList;
        applyQuery(tagsList);
    }

    private void applyQuery(List<Tag> tagsList){
        searchList = new ArrayList<>();
        searchList = tagsList;
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
    @Override
    public void showProgressBar() {
        avl.show();
        searchView.setEnabled(false);
    }

    @Override
    public void hideProgessBar() {
        avl.hide();
        searchView.setEnabled(true);
    }

    @Override
    public void filterTags(String query) {
        searchList = new ArrayList<>();
        if(!query.isEmpty()){
            tagsGroup.removeAllViews();
            for(Tag tag : this.tagList){
                if(tag.getTitle().contains(query)){
                    searchList.add(tag);
                }
            }
            applyQuery(searchList);
        }else{
            applyQuery(tagList);
        }
    }
}
