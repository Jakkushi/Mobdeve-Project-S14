package com.mobdeve.s14.group20.mobdeveproject;

import java.io.Serializable;

public class Item implements Serializable {

    private String noteType;

    public Item(String noteType){
        this.noteType = noteType;
    }

    public String getNoteType() {
        return this.noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }
}
