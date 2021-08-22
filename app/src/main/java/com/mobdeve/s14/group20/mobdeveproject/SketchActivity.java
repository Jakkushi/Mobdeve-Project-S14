package com.mobdeve.s14.group20.mobdeveproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class SketchActivity extends AppCompatActivity {

    private Button clearButton;
    private CanvasView canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch);

        clearButton = findViewById(R.id.sketch_clear_screen);
        canvas = findViewById(R.id.sketch_canvas_view);

        clearButton.setOnClickListener(v -> canvas.clearScreen());
    }
}