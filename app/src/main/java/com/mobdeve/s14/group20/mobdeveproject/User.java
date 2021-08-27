package com.mobdeve.s14.group20.mobdeveproject;

import java.util.ArrayList;

public class User {
    private String email;
    private String password;
    private ArrayList<Note> notes;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.notes = new ArrayList<>();
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public ArrayList<Note> getNotes() { return this.notes; }

}
