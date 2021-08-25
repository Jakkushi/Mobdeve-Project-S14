package com.mobdeve.s14.group20.mobdeveproject;

import android.content.Context;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotesDataHelper {

    public ArrayList<Note> initializeNotes(ArrayList<DatabaseNotesData> dbNotes, Context cxt) {

        ArrayList<Note> data = new ArrayList<>();

        Date currentDate = new Date();

        int numNotes = dbNotes.size();
        System.out.println("DATA HELPER TEST");
        System.out.println("numNotes: " + numNotes);
        DatabaseNotesData tempDbNote;

        for(int i= 0; i < numNotes; i++) {
            tempDbNote = dbNotes.get(i);

            if(tempDbNote.getNoteType().equals("ToDo")) {

                ArrayList<Item> todos = new ArrayList<>();
                int numTodos = tempDbNote.getTodo().size();

                for(int j = 0; j < numTodos; j++) {
                    boolean isDone = false;
                    ArrayList<String> tempTodo = tempDbNote.getTodo().get(j);
                    String isDoneString = tempTodo.get(0);

                    if(isDoneString.equals("true"))
                        isDone = true;

//                    System.out.println("act " + j + " : " + tempTodo.get(1));

                    todos.add(new ToDoItem(isDone, tempTodo.get(1)));
                }


                data.add(new Note(
                        tempDbNote.getTitle(),
                        tempDbNote.getSubtitle(),
                        tempDbNote.getNoteType(),
                        tempDbNote.getDateModified(),
                        tempDbNote.getTags(),
                        todos
                ));

            }
            else if(tempDbNote.getNoteType().equals("Interest")) {

                ArrayList<Item> interestItems = new ArrayList<>();
                int numItems = tempDbNote.getItems().size();

                System.out.println("Num Interest items: " + numItems);

                for(int j = 0; j < numItems; j++) {
                    ArrayList<String> tempInterestItem = tempDbNote.getItems().get(j);

                    System.out.println("photo " + j + " : " + tempInterestItem.get(0));
                    System.out.println("rating " + j + " : " + Integer.parseInt(String.valueOf(tempInterestItem.get(1))));
                    System.out.println("title " + j + " : " + tempInterestItem.get(2));
                    System.out.println("comment " + j + " : " + tempInterestItem.get(3));

                    interestItems.add(new InterestItem(tempInterestItem.get(0),
                            Integer.parseInt(String.valueOf(tempInterestItem.get(1))),
                            tempInterestItem.get(2),
                            tempInterestItem.get(3),
                            cxt));
                }

                data.add(new Note(
                        tempDbNote.getTitle(),
                        tempDbNote.getSubtitle(),
                        tempDbNote.getNoteType(),
                        tempDbNote.getDateModified(),
                        tempDbNote.getTags(),
                        interestItems
                ));
            }
            else if(tempDbNote.getNoteType().equals("Detailed")){

                ArrayList<Item> detailedItems = new ArrayList<>();
                int numItems = tempDbNote.getItems().size();

                System.out.println("Num Interest items: " + numItems);

                for(int j = 0; j < numItems; j++) {
                    ArrayList<String> tempInterestItem = tempDbNote.getItems().get(j);

                    System.out.println("photo " + j + " : " + tempInterestItem.get(0));
                    System.out.println("description " + j + " : " + tempInterestItem.get(1));
                    System.out.println("title " + j + " : " + tempInterestItem.get(2));
                    System.out.println("comment " + j + " : " + tempInterestItem.get(3));

                    detailedItems.add(new DetailedItem(tempInterestItem.get(0),
                            tempInterestItem.get(1),
                            tempInterestItem.get(2),
                            tempInterestItem.get(3),
                            cxt));
                }

                data.add(new Note(
                        tempDbNote.getTitle(),
                        tempDbNote.getSubtitle(),
                        tempDbNote.getNoteType(),
                        tempDbNote.getDateModified(),
                        tempDbNote.getTags(),
                        detailedItems
                ));

            }
            else{

                data.add(new Note(
                        tempDbNote.getTitle(),
                        tempDbNote.getSubtitle(),
                        tempDbNote.getNoteType(),
                        tempDbNote.getDateModified(),
                        tempDbNote.getTags()
                ));
            }
        }

//        data.add(new Note(
//                "Groceries 2",
//                "Vegetables",
//                "To Do",
//                "11/02/21",
//                new ArrayList<String>() {{
//                    add("#TAG");
//                    add("#VEGGIES");
//                }},
//                new ArrayList<Item>(){{
//                    add(new ToDoItem(false, "Carrot"));
//                    add(new ToDoItem(true, "Ginger"));
//                    add(new ToDoItem(true, "Lettuce"));
//                }}
//        ));
//
//        data.add(new Note(
//                "Sketch #1",
//                "Knight",
//                "Sketchbook",
//                "11/02/21",
//                new ArrayList<String>() {{
//                    add("#BOOKS");
//                    add("#PAGES");
//                    add("#BOOKS");
//                    add("#PAGES");
//                    add("#BOOKS");
//                    add("#PAGES");
//                    add("#BOOKS");
//                    add("#PAGES");
//                }}
//        ));
//
//        data.add(new Note(
//                "TV Shows",
//                "NETFLIX",
//                "Interest",
//                "11/02/21",
//                new ArrayList<String>() {{
//                    add("#NETFLIX");
//                    add("#SHOWSILOVE");
//                }},
//                new ArrayList<Item>(){{
//                    add(new InterestItem("breaking_bad", 4, "A Series of Unfortunate Events", "What an amazing show.", cxt));
//                }}
//        ));
//
//        data.add(new Note(
//                "Important People",
//                "Don't Forget!",
//                "Detailed",
//                "01/03/19",
//                new ArrayList<String>() {{
//                    add("#DOCTOR");
//                    add("#WHO?");
//                }},
//                new ArrayList<Item>(){{
//                    add(new DetailedItem(
//                            "breaking_bad",
//                            "Chemistry Teacher",
//                            "Looks kinda shady ngl",
//                            "Met him in Chemistry Class grade 10, he suddenly went bald one " +
//                                    "day", cxt
//                    ));
//                }}
//        ));
//
//        data.add(new Note(
//                "TV Shows",
//                "NETFLIX",
//                "Interest",
//                "11/02/21",
//                new ArrayList<String>() {{
//                    add("#NETFLIX");
//                    add("#SHOWSILOVE");
//                }},
//                new ArrayList<Item>(){{
//                    add(new InterestItem("breaking_bad", 4, "A Series of Unfortunate Events", "What an amazing show.", cxt));
//                }}
//        ));

        return data;
    }
}