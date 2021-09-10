package com.mobdeve.s14.group20.mobdeveproject;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class DetailedItem extends Item implements Serializable {

    private String imgId;
    private String title, subtitle, text;

    public DetailedItem(String imgUrl, String title, String subtitle, String text, Context cxt) {
        super("Detailed");

//        String name = imgId;
//        int id = cxt.getResources().getIdentifier(name, "drawable", cxt.getPackageName());
//        Drawable drawable = cxt.getResources().getDrawable(id);

        this.imgId = imgUrl;
        this.title = title;
        this.subtitle = subtitle;
        this.text = text;
    }

    public String getImgId() {
        return this.imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
