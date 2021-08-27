package com.mobdeve.s14.group20.mobdeveproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class DisplayNotesActivity extends AppCompatActivity {

    private Context context;

    private Button clearButton;
    private Button saveButton;
    private ProgressBar pbMain;
    private CanvasView canvas;
    private FloatingActionButton fabAddTemplate;
    private SearchView svFilterNotes;

    private TextView titleView;
    private ProgressBar pbNotes;

    private RecyclerView rvNotes;
    private RecyclerView.LayoutManager notesManager;
    private NotesAdapter notesAdapater;

    private ArrayList<Note> notes;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private String userId;
    private FirebaseUser user;

    public ArrayList<DatabaseNotesData> dbNotes = new ArrayList<>();

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
        setContentView(R.layout.activity_main);

        this.svFilterNotes = findViewById(R.id.main_sv_search);
        this.svFilterNotes.setFocusable(false);
        this.svFilterNotes.setIconified(false);
        this.svFilterNotes.clearFocus();
        this.pbNotes = findViewById(R.id.pb_notes);

        hideUI();
        this.initFAB();
        this.initFirebase();
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

    private void initFAB(){

        fabAddTemplate = findViewById(R.id.main_fab_add_template);
        fabAddTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DisplayNotesActivity.this, TemplateListActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.user = mAuth.getCurrentUser();
        this.userId = user.getUid();
        this.reference = this.database.getReference().child(Collection.users.name()).child((this.userId)).child(Collection.notes.name());
    }

    private void initRecyclerView(){
        this.rvNotes = findViewById(R.id.main_rv_notes);
        this.pbMain = findViewById(R.id.pb_main);

        this.dbNotes = (ArrayList<DatabaseNotesData>) getIntent().getSerializableExtra(Keys.DBNOTES.name());
        Collections.reverse(this.dbNotes);
        int dblength = dbNotes.size();
        System.out.println("IN DNA: " + dblength);


        this.notesManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        System.out.println("INSIDE MAIN" + this);
        this.rvNotes.setLayoutManager(this.notesManager);

        NotesDataHelper helper = new NotesDataHelper();

        this.notes = helper.initializeNotes(dbNotes, this);

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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private Handler mHandler = new Handler();

    @Override
    protected void onRestart() {
        super.onRestart();

        dbNotes = new ArrayList<DatabaseNotesData>();

        this.reference.orderByChild(Collection.dateModified.name()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                ArrayList<ArrayList<String>> interestItems = new ArrayList<>();
                ArrayList<String> tags = new ArrayList<>();
                ArrayList<ArrayList<String>> todoList = new ArrayList<>();
                ArrayList<ArrayList<String>> blankItems = new ArrayList<>();

                try {
                    interestItems =  (ArrayList) (( (HashMap) snapshot.getValue()).get("interestItem"));
                }
                catch (Exception e){
                    interestItems = null;
                    Log.w("error", "No Interest items in entry");
                }

                try {
                    tags = (ArrayList) (((HashMap) snapshot.getValue()).get("tags"));
                }
                catch (Exception d){
                    tags = null;
                    Log.w("error", "No Tags in entry");
                }

                try {
                    todoList = (ArrayList) (( (HashMap) snapshot.getValue()).get("todo"));
                } catch (Exception f) {
                    todoList = null;
                    Log.w("error", "No Todos in entry");
                }

                try {
                    blankItems = (ArrayList) (((HashMap) snapshot.getValue()).get("blankItems"));
                }
                catch (Exception d){
                    tags = null;
                    Log.w("error", "No Blank items in entry");
                }

                Log.d("Snapshot Key: ", snapshot.getKey());

                dbNotes.add(new DatabaseNotesData( (String) (( (HashMap) snapshot.getValue()).get("title")),
                        (String) ((HashMap) snapshot.getValue()).get("subtitle"), (String) ((HashMap) snapshot.getValue()).get("noteType"),
                        (String) ((HashMap) snapshot.getValue()).get("dateModified"), interestItems, tags, todoList, blankItems, snapshot.getKey()));

                pbNotes.setVisibility(View.VISIBLE);
                Intent intent = new Intent(DisplayNotesActivity.this, DisplayNotesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                Collections.reverse(dbNotes);
                intent.putExtra(Keys.DBNOTES.name(), dbNotes);
                startActivity(intent);
                finish();
                pbNotes.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });


    }

    private Runnable restartDashboard = new Runnable() {
        public void run() {
            pbNotes.setVisibility(View.GONE);
            Intent intent = new Intent(DisplayNotesActivity.this, DisplayNotesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Keys.DBNOTES.name(), dbNotes);
            startActivity(intent);
            finish();
        }
    };

}