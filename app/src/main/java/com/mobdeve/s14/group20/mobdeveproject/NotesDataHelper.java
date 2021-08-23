package com.mobdeve.s14.group20.mobdeveproject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotesDataHelper {

    public ArrayList<Note> initializeNotes() {

        ArrayList<Note> data = new ArrayList<>();

        Date currentDate = new Date();

        data.add(new Note(
                "Groceries",
                "Vegetables",
                "ToDo",
                "11/02/21",
                new ArrayList<String>() {{
                    add("#TAG");
                    add("#VEGGIES");
                }}
        ));

        data.add(new Note(
                "Band Practice",
                "The Foo Fighters",
                "Detailed",
                "12/22/15",
                new ArrayList<String>() {{
                    add("#DRUMS");
                    add("#GUITAR");
                    add("#SINGER");
                    add("#DRUMS");
                    add("#GUITAR");
                    add("#SINGER");
                }}
        ));

        data.add(new Note(
                "Sketch #1",
                "Knight",
                "Sketchbook",
                "05/21/19",
                new ArrayList<String>() {{
                    add("#SKETCH");
                    add("#PEN");
                }}
        ));

        data.add(new Note(
                "Movies to Watch",
                "Netflix",
                "Interest",
                "09/11/20",
                new ArrayList<String>() {{
                    add("#RE");
                    add("#BOKUNOPICO");
                }}
        ));

        data.add(new Note(
                "Movies to Watch",
                "Netflix",
                "Interest",
                "09/11/20",
                new ArrayList<String>() {{
                    add("#ATLA");
                    add("#COMEDY");
                }}
        ));

        data.add(new Note(
                "Last One",
                "Netflix",
                "Interest",
                "09/11/20",
                new ArrayList<String>() {{
                    add("#HORROR");
                    add("#THRILLER");
                }}
        ));

        return data;
    }
}
