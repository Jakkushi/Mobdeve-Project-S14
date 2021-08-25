package com.mobdeve.s14.group20.mobdeveproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class IndivNoteActivity extends AppCompatActivity {

    private TextView tvTitle, tvSubtitle;
    private RecyclerView rvIndivNotes, rvIndivTags;
    private RecyclerView.LayoutManager indivNotesManager, indivTagsManager;
    private IndivNotesAdapter indivNotesAdapter;
    private IndivTagsAdapter indivTagsAdapter;

    private String title, subtitle, noteType;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indiv_note);

        this.loadData();
        this.bindEssentials();
        this.initRecyclerView();
    }

    private void bindEssentials(){

        this.tvTitle = findViewById(R.id.et_indiv_title);
        this.tvSubtitle = findViewById(R.id.et_indiv_subtitle);

        this.tvTitle.setText(this.title);
        this.tvSubtitle.setText(this.subtitle);
    }

    private void initRecyclerView(){
        this.rvIndivNotes = findViewById(R.id.indiv_rv_templates);
        this.indivNotesManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.rvIndivNotes.setLayoutManager(this.indivNotesManager);
        this.indivNotesAdapter = new IndivNotesAdapter(this.title, this.tags, this.items);
        this.rvIndivNotes.setAdapter(this.indivNotesAdapter);

        this.rvIndivTags = findViewById(R.id.rv_indiv_tags);
        this.indivTagsManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.rvIndivTags.setLayoutManager(indivTagsManager);
        this.indivTagsAdapter = new IndivTagsAdapter(this.tags);
        this.rvIndivTags.setAdapter(this.indivTagsAdapter);
    }

    private void loadData(){

        this.title = getIntent().getStringExtra(Keys.TITLE.name());
        this.subtitle = getIntent().getStringExtra(Keys.SUBTITLE.name());
        this.noteType = getIntent().getStringExtra(Keys.NOTETYPE.name());
        this.tags = getIntent().getStringArrayListExtra(Keys.TAGS.name());
        this.items = (ArrayList<Item>) getIntent().getSerializableExtra(Keys.ITEMS.name());

        Log.d("TITLE", this.title);
        Log.d("SUBTITLE", this.subtitle);
        Log.d("NOTETYPE", this.noteType);
        Log.d("TAGS", String.valueOf(this.tags));
        Log.d("TODOITEMS", String.valueOf(this.items));
    }
}