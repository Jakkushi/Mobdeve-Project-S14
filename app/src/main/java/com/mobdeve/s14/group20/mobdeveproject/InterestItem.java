package com.mobdeve.s14.group20.mobdeveproject;

import java.io.Serializable;

public class InterestItem extends Item implements Serializable {

    private int imgId;
    private float rating;
    private String title, text;

    public InterestItem(int imgId, int rating, String title, String text){
        super("Interest");
        this.imgId = imgId;
        this.rating = rating;
        this.title = title;
        this.text = text;
    }

    public int getImgId() {
        return this.imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
