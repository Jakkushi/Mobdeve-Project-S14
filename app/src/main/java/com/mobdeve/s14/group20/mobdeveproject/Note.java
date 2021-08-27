package com.mobdeve.s14.group20.mobdeveproject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Note {

    private String title, subtitle, noteType, dateModified;
    private ArrayList<String> tags;
    private ArrayList<Item> items;
    private int numBlanks;
    private ArrayList<Item> blankItems;
    private String noteId;

    public Note(String title, String subtitle, String noteType, String dateModified, ArrayList<String> tags, ArrayList<Item> items, String noteId) {
        this.title = title;
        this.subtitle = subtitle;
        this.noteType = noteType;
        this.dateModified = dateModified;
        this.tags = tags;
        this.items = items;
        this.noteId = noteId;
    }

    public Note(String title, String subtitle, String noteType, String dateModified, ArrayList<String> tags, String noteId) {
        this.title = title;
        this.subtitle = subtitle;
        this.noteType = noteType;
        this.dateModified = dateModified;
        this.tags = tags;
        this.noteId = noteId;
    }

    public Note(String title, String subtitle, String noteType, String dateModified, ArrayList<String> tags, int numBlanks, ArrayList<Item> blankItems, String noteId) {
        this.title = title;
        this.subtitle = subtitle;
        this.noteType = noteType;
        this.dateModified = dateModified;
        this.tags = tags;
        this.numBlanks = numBlanks;
        this.blankItems = blankItems;
        this.noteId = noteId;
    }

    public String getTitle(){ return this.title; }

    public String getSubtitle(){ return this.subtitle; }

    public String getNoteType(){ return this.noteType; }

    public String getDateModified(){ return this.dateModified; }

    public ArrayList<Item> getItems(){ return this.items; }

    public ArrayList<String> getTags(){ return this.tags; }

    public String getNoteId(){ return this.noteId; }

    public void setTitle(String title){ this.title = title; }

    public void setSubtitle(String subtitle){ this.subtitle = subtitle; }

    public void setDateModified(String dateModified){ this.dateModified = dateModified; }

    public void addTag(String tag){ tags.add(tag); }

    public void setNoteId(String id){ this.noteId = id; }

    public ArrayList<Item> getBlankItems() {
        return this.blankItems;
    }

    public void setBlankItems(ArrayList<Item> blankItems) {
        this.blankItems = blankItems;
    }
}
