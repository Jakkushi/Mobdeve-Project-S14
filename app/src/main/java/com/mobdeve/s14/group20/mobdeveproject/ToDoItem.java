package com.mobdeve.s14.group20.mobdeveproject;

public class ToDoItem {

    private boolean isDone;
    private String text;
    private int noteId;

    public ToDoItem(boolean isDone, String text, int noteId){
        this.isDone = isDone;
        this.text = text;
        this.noteId = noteId;
    }

    public boolean getIsDone(){ return this.isDone; }

    public String getText(){ return this.text; }

    public int getNoteId(){ return this.noteId; }

    public void setIsDone(boolean isDone){ this.isDone = isDone; }

    public void setText(String text){ this.text = text; }

    public void setNoteId(int noteId){ this.noteId = noteId; }
}
