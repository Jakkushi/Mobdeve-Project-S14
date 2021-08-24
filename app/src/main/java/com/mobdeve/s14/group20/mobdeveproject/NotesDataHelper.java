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
                new ArrayList<Item>(){{
                    add(new ToDoItem(false, "Carrot"));
                    add(new ToDoItem(true, "Ginger"));
                    add(new ToDoItem(true, "Lettuce"));
                }}
        ));

        data.add(new Note(
                "Sketch #1",
                "Knight",
                "Sketchbook",
                "11/02/21",
                new ArrayList<String>() {{
                    add("#BOOKS");
                    add("#PAGES");
                    add("#BOOKS");
                    add("#PAGES");
                    add("#BOOKS");
                    add("#PAGES");
                    add("#BOOKS");
                    add("#PAGES");
                }}
        ));

        data.add(new Note(
                "TV Shows",
                "NETFLIX",
                "Interest",
                "11/02/21",
                new ArrayList<String>() {{
                    add("#NETFLIX");
                    add("#SHOWSILOVE");
                }},
                new ArrayList<Item>(){{
                    add(new InterestItem(R.drawable.breaking_bad, 4, "A Series of Unfortunate Events", "What an amazing show."));
                }}
        ));

        data.add(new Note(
                "Important People",
                "Don't Forget!",
                "Detailed",
                "01/03/19",
                new ArrayList<String>() {{
                    add("#DOCTOR");
                    add("#WHO?");
                }},
                new ArrayList<Item>(){{
                    add(new DetailedItem(
                            R.drawable.breaking_bad,
                            "Chemistry Teacher",
                            "Looks kinda shady ngl",
                            "Met him in Chemistry Class grade 10, he suddenly went bald one " +
                                    "day"
                    ));
                }}
        ));

        return data;
    }
}
