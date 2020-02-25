package com.devahmed.demo.animeapp.ui.search;

import android.view.View;

import com.devahmed.demo.animeapp.Models.Tag;

import java.util.List;

public interface SearchMvc {

    public interface Listener{
        void onQueryChange(String text);
        void onTagClicked(String tag);
        void onSearchViewFocusChanged(View view , boolean b);
    }

    void bindTagsData(List<Tag> tagsList);
    void showProgressBar();
    void hideProgessBar();
    void filterTags(String query);

}
