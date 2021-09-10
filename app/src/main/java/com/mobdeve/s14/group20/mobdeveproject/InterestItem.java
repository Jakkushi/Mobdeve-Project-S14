package com.mobdeve.s14.group20.mobdeveproject;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.lang.reflect.Field;

public class InterestItem extends Item implements Serializable {

    private String imgId;
    private float rating;
    private String title, text;

    public InterestItem(String imgUrl, float rating, String title, String text, Context cxt){
        super("Interest");
//        String name = imgId;
//        int id = cxt.getResources().getIdentifier(name, "drawable", cxt.getPackageName());
//        Drawable drawable = cxt.getResources().getDrawable(id);
        this.imgId = imgUrl;
        this.rating = rating;
        this.title = title;
        this.text = text;
    }

    public String getImgId() {
        return this.imgId;
    }

    public void setImgId(String imgId) {
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