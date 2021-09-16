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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class DisplayNotesActivity extends AppCompatActivity {

    private Context context;

    private Button btnReset;
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

        this.btnReset = findViewById(R.id.main_btn_reset);
        this.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbNotes = new ArrayList<DatabaseNotesData>();

                reference.orderByChild(Collection.dateModified.name()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        ArrayList<ArrayList<String>> interestItems = new ArrayList<>();
                        ArrayList<ArrayList<String>> lessonItems = new ArrayList<>();
                        ArrayList<String> tags = new ArrayList<>();
                        ArrayList<ArrayList<String>> todoList = new ArrayList<>();
                        ArrayList<ArrayList<String>> blankItems = new ArrayList<>();
                        String sketchURL = null;

                        try {
                            interestItems = (ArrayList) (((HashMap) snapshot.getValue()).get("interestItem"));
                        } catch (Exception e) {
                            interestItems = null;
                            Log.w("error", "No Interest items in entry");
                        }

                        try {
                            tags = (ArrayList) (((HashMap) snapshot.getValue()).get("tags"));
                        } catch (Exception d) {
                            tags = null;
                            Log.w("error", "No Tags in entry");
                        }

                        try {
                            todoList = (ArrayList) (((HashMap) snapshot.getValue()).get("todo"));
                        } catch (Exception f) {
                            todoList = null;
                            Log.w("error", "No Todos in entry");
                        }

                        try {
                            blankItems = (ArrayList) (((HashMap) snapshot.getValue()).get("blankItems"));
                        } catch (Exception d) {
                            tags = null;
                            Log.w("error", "No Blank items in entry");
                        }

                        try {
                            lessonItems = (ArrayList) (((HashMap) snapshot.getValue()).get("lessonNotesItem"));
                        } catch (Exception d) {
                            tags = null;
                            Log.w("error", "No Blank items in entry");
                        }

                        try {
                            sketchURL = (String) (((HashMap) snapshot.getValue()).get("sketchLink"));

                            Log.w("SKETCH DATATYPE", sketchURL);
                        } catch (Exception d) {
                            Log.w("error", "No sketch items in entry");
                        }

                        Log.d("Snapshot Key: ", snapshot.getKey());

                        dbNotes.add(new DatabaseNotesData((String) (((HashMap) snapshot.getValue()).get("title")),
                                (String) ((HashMap) snapshot.getValue()).get("subtitle"), (String) ((HashMap) snapshot.getValue()).get("noteType"),
                                (String) ((HashMap) snapshot.getValue()).get("dateModified"), interestItems, tags, todoList, blankItems, lessonItems, sketchURL, snapshot.getKey()));

                        pbNotes.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(DisplayNotesActivity.this, DisplayNotesActivity.class);

                        intent.putExtra(Keys.DBNOTES.name(), dbNotes);
                        startActivity(intent);

                        pbNotes.setVisibility(View.GONE);
                        finish();
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
        });

        hideUI();
        this.initFAB();
        this.initFirebase();
        this.initRecyclerView();

        this.context = this;

        this.svFilterNotes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String[] options = {"Search by title", "Search by tag", "Cancel"};
                new AlertDialog.Builder(context)
                        .setTitle("Choose an Action")
                        .setItems(options, (dialog, which) -> {
                            switch(which) {
                                case 0:
                                    searchNotes(query, 0);
                                    break;
                                case 1:
                                    searchNotes(query, 1);
                                    break;
                            }
                        }).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void initFAB(){

        fabAddTemplate = findViewById(R.id.main_fab_add_template);
        fabAddTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DisplayNotesActivity.this, TemplateListActivity.class);
                v.getContext().startActivity(intent);
                finish();
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
                ArrayList<ArrayList<String>> lessonItems = new ArrayList<>();
                ArrayList<String> tags = new ArrayList<>();
                ArrayList<ArrayList<String>> todoList = new ArrayList<>();
                ArrayList<ArrayList<String>> blankItems = new ArrayList<>();
                String sketchURL = null;

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

                try {
                    lessonItems = (ArrayList) (((HashMap) snapshot.getValue()).get("lessonNotesItem"));
                }
                catch (Exception d){
                    tags = null;
                    Log.w("error", "No Blank items in entry");
                }

                try{
                    sketchURL = (String) (((HashMap) snapshot.getValue()).get("sketchLink"));

                    Log.w("SKETCH DATATYPE", sketchURL);
                }
                catch(Exception d){
                    Log.w("error", "No sketch items in entry");
                }

                Log.d("Snapshot Key: ", snapshot.getKey());

                dbNotes.add(new DatabaseNotesData( (String) (( (HashMap) snapshot.getValue()).get("title")),
                        (String) ((HashMap) snapshot.getValue()).get("subtitle"), (String) ((HashMap) snapshot.getValue()).get("noteType"),
                        (String) ((HashMap) snapshot.getValue()).get("dateModified"), interestItems, tags, todoList, blankItems, lessonItems, sketchURL, snapshot.getKey()));

                pbNotes.setVisibility(View.VISIBLE);
                Intent intent = new Intent(DisplayNotesActivity.this, DisplayNotesActivity.class);

                intent.putExtra(Keys.DBNOTES.name(), dbNotes);
                startActivity(intent);

                pbNotes.setVisibility(View.GONE);
                finish();
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

    private void searchNotes(String query, int queryType) {
        dbNotes = new ArrayList<DatabaseNotesData>();
        Log.d("TEST QUERY", query);

        Query dbQuery;

        if(queryType == 0)
            dbQuery = this.reference.orderByChild("title").startAt(query).endAt(query+"\uf8ff");
        else
            dbQuery = this.reference.orderByChild("tags/0").startAt("#" + query.toUpperCase()).endAt("#" + query.toUpperCase()+"\uf8ff");

        dbQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    HashMap map = ((HashMap)snapshot.getValue());

                    for (Object key : map.keySet()) {
                        ArrayList<ArrayList<String>> interestItems = new ArrayList<>();
                        ArrayList<ArrayList<String>> lessonItems = new ArrayList<>();
                        ArrayList<String> tags = new ArrayList<>();
                        ArrayList<ArrayList<String>> todoList = new ArrayList<>();
                        ArrayList<ArrayList<String>> blankItems = new ArrayList<>();
                        String sketchLink = null;

                        HashMap tempMap = ((HashMap) map.get(key));
                        String title = (String) tempMap.get("title");
                        System.out.println(tempMap.get("title"));

                        String subtitle = (String) tempMap.get("subtitle");
                        System.out.println(tempMap.get("subtitle"));

                        String noteType = (String) tempMap.get("noteType");
                        System.out.println(tempMap.get("subtitle"));

                        String dateModified = (String) tempMap.get("dateModified");
                        System.out.println(tempMap.get("dateModified"));

                        tags = (ArrayList) tempMap.get("tags");
                        System.out.println((ArrayList) tempMap.get("tags"));

                        if(noteType.equals("Blank")){
                            blankItems = (ArrayList) tempMap.get("blankItems");
                            System.out.println((ArrayList) tempMap.get("blankItems"));
                        }
                        else if(noteType.equals("ToDo")){
                            todoList = (ArrayList) tempMap.get("todo");
                            System.out.println((ArrayList) tempMap.get("todo"));
                        }
                        else if(noteType.equals("Interest")){
                            interestItems = (ArrayList) tempMap.get("interestItem");
                            System.out.println((ArrayList) tempMap.get("interestItem"));
                        }
                        else if(noteType.equals("Detailed")){
                            blankItems = (ArrayList) tempMap.get("interestItem");
                            System.out.println((ArrayList) tempMap.get("interestItem"));
                        }
                        else if(noteType.equals("Lesson")){
                            lessonItems = (ArrayList) tempMap.get("lessonNotesItem");
                            System.out.println((ArrayList) tempMap.get("lessonNotesItem"));
                        }
                        else if(noteType.equals("Sketchbook")) {
                            sketchLink = (String) tempMap.get("sketchLink");
                            System.out.println(tempMap.get("sketchLink"));
                        }

                        dbNotes.add(new DatabaseNotesData( title, subtitle, noteType, dateModified, interestItems,
                                tags, todoList, blankItems, lessonItems, sketchLink, (String) key));


                    }
                }

                pbNotes.setVisibility(View.VISIBLE);
                Intent intent = new Intent(DisplayNotesActivity.this, DisplayNotesActivity.class);

                intent.putExtra(Keys.DBNOTES.name(), dbNotes);
                startActivity(intent);

                pbNotes.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}