package com.mobdeve.s14.group20.mobdeveproject;

import java.io.Serializable;

public class DetailedItem extends Item implements Serializable {

    private int imgId;
    private String title, subtitle, text;

    public DetailedItem(int imgId, String title, String subtitle, String text) {
        super("Detailed");

        this.imgId = imgId;
        this.title = title;
        this.subtitle = subtitle;
        this.text = text;
    }

    public int getImgId() {
        return this.imgId;
    }

    public void setImgId(int imgId) {
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
