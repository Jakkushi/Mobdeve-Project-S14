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
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private Button clearButton;
    private Button saveButton;
    private CanvasView canvas;

    private TextView titleView;

    private RecyclerView rvNotes;
    private RecyclerView.LayoutManager notesManager;
    private NotesAdapter notesAdapater;

    private ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initRecyclerView();

//        context = getApplicationContext();
//        clearButton = findViewById(R.id.sketch_clear_screen);
//        saveButton = findViewById(R.id.sketch_save_button);
//        canvas = findViewById(R.id.sketch_canvas_view);
//        titleView = findViewById(R.id.sketch_tv_title);
//
//        clearButton.setOnClickListener(v -> canvas.clearScreen());
//        saveButton.setOnClickListener(v -> {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                CharSequence text;
//                try {
//                    canvas.saveScreen(titleView.getText());
//                    text = "Sketch saved successfully!";
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    text = "Unable to save sketch";
//                }
//                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
//                toast.show();
//            } else {
//                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            }
//        });
    }

    private void initRecyclerView(){
        this.rvNotes = findViewById(R.id.main_rv_notes);

        this.notesManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        System.out.println("INSIDE MAIN" + this);
        this.rvNotes.setLayoutManager(this.notesManager);

        NotesDataHelper helper = new NotesDataHelper();
        this.notes = helper.initializeNotes();

        this.notesAdapater = new NotesAdapter(this.notes);
        this.rvNotes.setAdapter(this.notesAdapater);
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                CharSequence text;
                try {
                    canvas.saveScreen(titleView.getText());
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