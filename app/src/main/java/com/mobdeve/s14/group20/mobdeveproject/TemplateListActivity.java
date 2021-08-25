package com.mobdeve.s14.group20.mobdeveproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;

public class TemplateListActivity extends AppCompatActivity {

    private EditText etTitle, etSubtitle, etTags;
    private ImageButton ibBlank, ibToDo, ibDetailed, ibLesson, ibSketch, ibInterest;

    private String title, subtitle, tagsTemp;
    private ArrayList<String> tags;


    private void hideUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_template_list);
        hideUI();
        this.initElements();
    }

    private void initElements(){

        this.etTitle = findViewById(R.id.et_template_list_title);
        this.etSubtitle = findViewById(R.id.et_template_subtitle);
        this.etTags = findViewById(R.id.et_template_tags);

        this.ibBlank = findViewById(R.id.ib_blank_template);
        this.ibBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICKED BLANK");
                getData();
                addTemplate("Blank");
            }
        });

        this.ibToDo = findViewById(R.id.ib_to_do_template);
        this.ibToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                addTemplate("ToDo");
            }
        });

        this.ibLesson = findViewById(R.id.ib_lesson_template);
        this.ibLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                addTemplate("Lesson");
            }
        });

        this.ibDetailed = findViewById(R.id.ib_detailed_template);
        this.ibDetailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                addTemplate("Detailed");
            }
        });

        this.ibSketch = findViewById(R.id.ib_sketch_template);
        this.ibSketch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                addTemplate("Sketchbook");
            }
        });

        this.ibInterest = findViewById(R.id.ib_interest_template);
        this.ibInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                addTemplate("Interest");
            }
        });

    }

    private void getData(){
        System.out.println("IN GET DATA");
        this.title = String.valueOf(this.etTitle.getText());
        System.out.println("TITLE VALUE" + this.title);
        this.subtitle = String.valueOf(this.etSubtitle.getText());
        this.tagsTemp = String.valueOf(this.etTags.getText());

        String str[] = tagsTemp.split(" ");
        ArrayList<String> temp = new ArrayList<>(Arrays.asList(str));
        this.tags = temp;
    }

    private void addTemplate(String noteType){

        System.out.println("IN ADD TEMPLATE");

        Log.d("Title", this.title);
        Log.d("Subtitle", this.subtitle);

        for(int i = 0; i < this.tags.size(); i++){
            Log.d("tag " + i,  this.tags.get(i));
        }

        Log.d("NoteType", noteType);

        ArrayList<Item> items = new ArrayList<>();

        if(noteType.equals("Blank")){
            Item newItem = new BlankItem("");
            items.add(newItem);
        }

        Intent intent = new Intent(TemplateListActivity.this, IndivNoteActivity.class);
        intent.putExtra(Keys.TITLE.name(), this.title);
        intent.putExtra(Keys.SUBTITLE.name(), this.subtitle);
        intent.putExtra(Keys.NOTETYPE.name(), noteType);
        intent.putExtra(Keys.TAGS.name(), this.tags);
        intent.putExtra(Keys.ITEMS.name(), items);
        startActivity(intent);
    }
}