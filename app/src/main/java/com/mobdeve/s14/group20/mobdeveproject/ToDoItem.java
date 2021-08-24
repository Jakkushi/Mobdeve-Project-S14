package com.mobdeve.s14.group20.mobdeveproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ToDoItem extends Item implements Serializable {

    private boolean isDone;
    private String text;

    public ToDoItem(boolean isDone, String text){
        super("ToDo");
        this.isDone = isDone;
        this.text = text;
    }

    public boolean getIsDone(){ return this.isDone; }

    public String getText(){ return this.text; }

//    public int getNoteId(){ return this.noteId; }

    public void setIsDone(boolean isDone){ this.isDone = isDone; }

    public void setText(String text){ this.text = text; }

//    public void setNoteId(int noteId){ this.noteId = noteId; }
}
