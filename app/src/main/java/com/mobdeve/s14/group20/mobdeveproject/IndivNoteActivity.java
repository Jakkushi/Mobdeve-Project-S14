package com.mobdeve.s14.group20.mobdeveproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class IndivNoteActivity extends AppCompatActivity {

    private TextView tvTitle, tvSubtitle;
    private RecyclerView rvIndivNotes;
    private RecyclerView.LayoutManager indivNotesManager;
    private IndivNotesAdapter indivNotesAdapter;

    private String title, subtitle, noteType;
    private ArrayList<String> tags;
    private ArrayList<Boolean> isDoneList;
    private ArrayList<String> textList;

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indiv_note);

        this.loadData();
        this.initRecyclerView();

        this.sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.spEditor = this.sp.edit();
    }

    private void initRecyclerView(){
        this.rvIndivNotes = findViewById(R.id.indiv_rv_templates);

        this.indivNotesManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.rvIndivNotes.setLayoutManager(this.indivNotesManager);

        this.indivNotesAdapter = new IndivNotesAdapter(tags, isDoneList, textList);
        this.rvIndivNotes.setAdapter(this.indivNotesAdapter);
    }

    private void loadData(){

        this.noteType = sp.getString(Keys.TODO_NOTETYPE.name(), "None");

        if(noteType.equals("ToDo")){
            this.title = this.sp.getString(Keys.TODO_TITLE.name(), "[Title]");
            this.subtitle = this.sp.getString(Keys.TODO_SUBTITLE.name(), "[Subtitle]");

            for(int i = 0; i < this.sp.getInt(Keys.TODO_ITEMS_LENGTH.name(), 0); i++){
                isDoneList.add(this.sp.getBoolean(Keys.TODO_ITEMS.name() + i + "checkbox", false));
                textList.add(this.sp.getString(Keys.TODO_ITEMS.name() + i + "string", "ERROR!"));
            }

            for(int i = 0; i < this.sp.getInt(Keys.TODO_TAGS_LENGTH.name(), 0); i++){
                tags.add(this.sp.getString(Keys.TODO_TAGS.name() + i, "ERROR"));
            }
        }
    }
}