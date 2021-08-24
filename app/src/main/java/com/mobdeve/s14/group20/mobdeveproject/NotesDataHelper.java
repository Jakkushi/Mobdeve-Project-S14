package com.mobdeve.s14.group20.mobdeveproject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotesDataHelper {

    public ArrayList<Note> initializeNotes() {

        ArrayList<Note> data = new ArrayList<>();

        Date currentDate = new Date();

        data.add(new Note(
                "Groceries 2",
                "Vegetables",
                "ToDo",
                "11/02/21",
                new ArrayList<String>() {{
                    add("#TAG");
                    add("#VEGGIES");
                }},
                new ArrayList<ToDoItem>(){{
                    add(new ToDoItem(false, "Carrot", 0));
                    add(new ToDoItem(true, "Ginger", 0));
                }}
        ));

        return data;
    }
}
