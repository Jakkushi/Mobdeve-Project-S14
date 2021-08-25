package com.mobdeve.s14.group20.mobdeveproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SketchActivity extends AppCompatActivity {

    private Context context;

    private RecyclerView rvTags;
    private RecyclerView.LayoutManager tagsManager;
    private TagsAdapter tagsAdapter;

    private Button clearButton;
    private Button saveButton;
    private CanvasView canvas;

    private String title, subtitle, noteType;
    private ArrayList<String> tags;

    private EditText etTitle, etSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch);

        context = getApplicationContext();
        clearButton = findViewById(R.id.sketch_clear_screen);
        saveButton = findViewById(R.id.sketch_save_button);
        canvas = findViewById(R.id.sketch_canvas_view);

        this.loadData();
        this.initEssentials();

        clearButton.setOnClickListener(v -> canvas.clearScreen());
        saveButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                CharSequence text;
                try {
                    canvas.saveScreen(etTitle.getText());
                    text = "Sketch saved successfully!";
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    text = "Unable to save sketch";
                }
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });
    }

    private void loadData(){

        this.title = getIntent().getStringExtra(Keys.TITLE.name());
        this.subtitle = getIntent().getStringExtra(Keys.SUBTITLE.name());
        this.tags = getIntent().getStringArrayListExtra(Keys.TAGS.name());
        this.noteType = getIntent().getStringExtra(Keys.NOTETYPE.name());
    }

    private void initEssentials(){

        this.etTitle = findViewById(R.id.etml_sketch_title);
        this.etSubtitle = findViewById(R.id.etml_sketch_subtitle);

        this.etTitle.setText(this.title);
        this.etSubtitle.setText(this.subtitle);

        this.rvTags = findViewById(R.id.rv_sketch_tags);
        this.tagsManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.rvTags.setLayoutManager(tagsManager);

        this.tagsAdapter = new TagsAdapter(this.tags);
        this.rvTags.setAdapter(this.tagsAdapter);
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    CharSequence text;
                    try {
                        canvas.saveScreen(etTitle.getText());
                        text = "Sketch saved successfully!";
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        text = "Unable to save sketch";
                    }
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Unable to save image without permissions.", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
}