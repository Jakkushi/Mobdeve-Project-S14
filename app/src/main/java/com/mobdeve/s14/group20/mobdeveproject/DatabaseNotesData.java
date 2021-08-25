package com.mobdeve.s14.group20.mobdeveproject;

import java.io.Serializable;
import java.util.ArrayList;

//DatabaseNotesData contains fields for all types of notes
public class DatabaseNotesData implements Serializable {

    private String title, subtitle, noteType, dateModified;
    private ArrayList<String> tags;
    private ArrayList<ArrayList<String>> items;
    private ArrayList<ArrayList<String>> todoList;

    public DatabaseNotesData(String title, String subtitle, String noteType, String dateModified,
                             ArrayList<ArrayList<String>> items, ArrayList<String> tags,
                             ArrayList<ArrayList<String>> todoList) {
        this.title = title;
        this.subtitle = subtitle;
        this.noteType = noteType;
        this.dateModified = dateModified;
        this.tags = tags;
        this.items = items;
        this.todoList = todoList;
    }

    public String getTitle(){ return this.title; }

    public String getSubtitle(){ return this.subtitle; }

    public String getNoteType(){ return this.noteType; }

    public String getDateModified(){ return this.dateModified; }

    public ArrayList<ArrayList<String>> getItems(){ return this.items; }

    public ArrayList<String> getTags(){ return this.tags; }

    public ArrayList<ArrayList<String>> getTodo(){ return this.todoList; }

    public void setTitle(String title){ this.title = title; }

    public void setSubtitle(String subtitle){ this.subtitle = subtitle; }

    public void setDateModified(String dateModified){ this.dateModified = dateModified; }

    public void addTag(String tag){ tags.add(tag); }

    public void addItem(ArrayList<String> item){ items.add(item); }

    public void addTodo(ArrayList<String> todoItem){ todoList.add(todoItem); }
}
