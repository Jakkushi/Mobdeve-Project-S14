package com.mobdeve.s14.group20.mobdeveproject;

import java.io.Serializable;

public class LessonNotesItem extends Item implements Serializable {

    private String title, subtitle, text;

    public LessonNotesItem(String title, String subtitle, String text) {
        super("Lesson");

        this.title = title;
        this.subtitle = subtitle;
        this.text = text;
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
