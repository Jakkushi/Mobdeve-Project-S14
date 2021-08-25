package com.mobdeve.s14.group20.mobdeveproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class TemplateListActivity extends AppCompatActivity {

    private EditText etTitle, etSubtitle, etTags;
    private ImageButton ibBlank, ibToDo, ibDetailed, ibLesson, ibSketch, ibInterest;


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

    }
}