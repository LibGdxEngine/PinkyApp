package com.devahmed.demo.animeapp.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Tag {
    private String id;
    private String title;
    private int noOfImages;

    public Tag() {
    }


    public Tag(String title, int noOfImages) {
        this.title = title;
        this.noOfImages = noOfImages;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNoOfImages() {
        return noOfImages;
    }

    public void setNoOfImages(int noOfImages) {
        this.noOfImages = noOfImages;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("noOfImages", noOfImages);
        return result;
    }

}
