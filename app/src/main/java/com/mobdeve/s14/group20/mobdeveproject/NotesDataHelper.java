package com.mobdeve.s14.group20.mobdeveproject;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotesDataHelper {

    public ArrayList<Note> initializeNotes(ArrayList<DatabaseNotesData> dbNotes, Context cxt) {

        ArrayList<Note> data = new ArrayList<>();

        Date currentDate = new Date();

        int numNotes = dbNotes.size();

        DatabaseNotesData tempDbNote;

        for(int i= 0; i < numNotes; i++) {
            tempDbNote = dbNotes.get(i);

            if(tempDbNote.getNoteType().equals("ToDo")) {

                ArrayList<Item> todos = new ArrayList<>();

                try{
                    int numTodos = tempDbNote.getTodo().size();

                    for(int j = 0; j < numTodos; j++) {
                        boolean isDone = false;
                        ArrayList<String> tempTodo = tempDbNote.getTodo().get(j);
                        String isDoneString = tempTodo.get(0);

                        if(isDoneString.equals("true"))
                            isDone = true;

                        todos.add(new ToDoItem(isDone, tempTodo.get(1)));
                    }
                } catch (Exception e) {
                    Log.d("todo error: ", "no to do items");
                }



                data.add(new Note(
                        tempDbNote.getTitle(),
                        tempDbNote.getSubtitle(),
                        tempDbNote.getNoteType(),
                        tempDbNote.getDateModified(),
                        tempDbNote.getTags(),
                        todos,
                        tempDbNote.getNoteId()
                ));

            }
            else if(tempDbNote.getNoteType().equals("Interest")) {

                ArrayList<Item> interestItems = new ArrayList<>();

                try{
                    int numItems = tempDbNote.getItems().size();

                    for(int j = 0; j < numItems; j++) {
                        ArrayList<String> tempInterestItem = tempDbNote.getItems().get(j);

                        interestItems.add(new InterestItem(tempInterestItem.get(0),
                                Float.parseFloat(String.valueOf(tempInterestItem.get(1))),
                                tempInterestItem.get(2),
                                tempInterestItem.get(3),
                                cxt));
                    }
                } catch (Exception e) {
                    Log.d("interest items error: ", "no interest items");
                }


                data.add(new Note(
                        tempDbNote.getTitle(),
                        tempDbNote.getSubtitle(),
                        tempDbNote.getNoteType(),
                        tempDbNote.getDateModified(),
                        tempDbNote.getTags(),
                        interestItems,
                        tempDbNote.getNoteId()
                ));
            }
            else if(tempDbNote.getNoteType().equals("Detailed")){

                ArrayList<Item> detailedItems = new ArrayList<>();

                try{
                    int numItems = tempDbNote.getItems().size();

                    for(int j = 0; j < numItems; j++) {
                        ArrayList<String> tempInterestItem = tempDbNote.getItems().get(j);

                        detailedItems.add(new DetailedItem(tempInterestItem.get(0),
                                tempInterestItem.get(1),
                                tempInterestItem.get(2),
                                tempInterestItem.get(3),
                                cxt));
                    }
                } catch (Exception e) {
                    Log.d("detailed items error: ", "no detailed items");
                }


                data.add(new Note(
                        tempDbNote.getTitle(),
                        tempDbNote.getSubtitle(),
                        tempDbNote.getNoteType(),
                        tempDbNote.getDateModified(),
                        tempDbNote.getTags(),
                        detailedItems,
                        tempDbNote.getNoteId()
                ));

            }
            else if(tempDbNote.getNoteType().equals("Blank")){

                ArrayList<Item> blankItems = new ArrayList<>();
                int numItems = tempDbNote.getBlankItems().size();


                for(int j = 0; j < numItems; j++) {
                    ArrayList<String> tempBlankItems = tempDbNote.getBlankItems().get(j);

                    Log.d("IN THE LOOP: ", String.valueOf(tempBlankItems.get(0)));

                    blankItems.add(new BlankItem(tempBlankItems.get(0)));
                }

                data.add(new Note(
                        tempDbNote.getTitle(),
                        tempDbNote.getSubtitle(),
                        tempDbNote.getNoteType(),
                        tempDbNote.getDateModified(),
                        tempDbNote.getTags(),
                        numItems,
                        blankItems,
                        tempDbNote.getNoteId()
                ));
            }
            else if(tempDbNote.getNoteType().equals("Lesson")){
                ArrayList<Item> lessonItems = new ArrayList<>();

                try{
                    int numItems = tempDbNote.getLessonItems().size();

                    for(int j = 0; j < numItems; j++) {
                        ArrayList<String> tempLessonItem = tempDbNote.getLessonItems().get(j);

                        Log.d("lesson item", tempLessonItem.get(0) + tempLessonItem.get(1) + tempLessonItem.get(2));
                        lessonItems.add(new LessonNotesItem(tempLessonItem.get(0),
                                tempLessonItem.get(1),
                                tempLessonItem.get(2)));
                    }
                } catch (Exception e) {
                    Log.d("lesson items error: ", String.valueOf(e));
                }

                data.add(new Note(
                        tempDbNote.getTitle(),
                        tempDbNote.getSubtitle(),
                        tempDbNote.getNoteType(),
                        tempDbNote.getDateModified(),
                        tempDbNote.getTags(),
                        lessonItems,
                        tempDbNote.getNoteId()
                ));
            }
            else{
                data.add(new Note(
                        tempDbNote.getTitle(),
                        tempDbNote.getSubtitle(),
                        tempDbNote.getNoteType(),
                        tempDbNote.getDateModified(),
                        tempDbNote.getTags(),
                        tempDbNote.getNoteId()
                ));
            }
        }

        return data;
    }
}