package com.mobdeve.s14.group20.mobdeveproject;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.InterpolateOnScrollPositionChangeHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class ExistingIndivNoteActivity extends AppCompatActivity implements IndivNotesAdapter.callCamera{

    private TextView tvTitle, tvSubtitle;
    private RecyclerView rvIndivNotes, rvIndivTags;
    private RecyclerView.LayoutManager indivNotesManager, indivTagsManager;
    private IndivNotesAdapter indivNotesAdapter;
    private IndivTagsAdapter indivTagsAdapter;
    private TextView tvNoteId;
    private ProgressBar pb_indiv_note;
    private ImageButton note_ib_holder;
    private FloatingActionButton fabAddTemplate;

    private String title, subtitle, noteType, noteId, picturePath;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference reference;

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
        setContentView(R.layout.activity_indiv_note);
        this.hideUI();
        this.loadData();
        this.bindEssentials();
        this.initRecyclerView();
        this.initFirebase();
    }

    private void initFirebase() {
        this.database = FirebaseDatabase.getInstance();
        this.reference = this.database.getReference().child(Collection.users.name());
    }

    private void bindEssentials(){

        this.tvTitle = findViewById(R.id.et_indiv_title);
        this.tvSubtitle = findViewById(R.id.et_indiv_subtitle);
        this.tvNoteId = findViewById(R.id.tv_note_id);
        this.pb_indiv_note = findViewById(R.id.pb_indiv_note);
        this.fabAddTemplate = findViewById(R.id.indiv_fab_add);
        this.bindFabOnClick();

        this.tvTitle.setText(this.title);
        this.tvSubtitle.setText(this.subtitle);
        this.tvNoteId.setText(noteId);
    }

    private void bindFabOnClick(){
        this.fabAddTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Pressed!" + noteType + "/" + items.size());

                if(noteType.equals("Blank")){
                    Item newItem = new BlankItem("");
                    items.add(newItem);
                    indivNotesAdapter.notifyItemInserted(items.size() - 1);
                }
                else if(noteType.equals("ToDo")){
                    Item newItem = new ToDoItem(false, "");
                    items.add(newItem);
                    indivNotesAdapter.notifyItemInserted(items.size() - 1);
                }
                else if(noteType.equals("Interest")){
                    Item newItem = new InterestItem("default_image", 0, "", "", ExistingIndivNoteActivity.this);
                    items.add(newItem);
                    indivNotesAdapter.notifyItemInserted(items.size() - 1);
                }
                else if(noteType.equals("Detailed")){
                    Item newItem = new DetailedItem("default_image", "", "", "", ExistingIndivNoteActivity.this);
                    items.add(newItem);
                    indivNotesAdapter.notifyItemInserted(items.size() - 1);
                }
                else if(noteType.equals("Lesson")){
                    Item newItem = new LessonNotesItem("", "", "");
                    items.add(newItem);
                    indivNotesAdapter.notifyItemInserted(items.size() - 1);
                }
            }
        });
    }

    private void initRecyclerView(){
        this.rvIndivNotes = findViewById(R.id.indiv_rv_templates);
        this.indivNotesManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.rvIndivNotes.setLayoutManager(this.indivNotesManager);
        this.indivNotesAdapter = new IndivNotesAdapter(this.title, this.tags, items, this);
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
        this.noteId = getIntent().getStringExtra(Keys.ID.name());

        Log.d("TITLE", this.title);
        Log.d("SUBTITLE", this.subtitle);
        Log.d("NOTETYPE", this.noteType);
        Log.d("TAGS", String.valueOf(this.tags));
        Log.d("TODOITEMS", String.valueOf(this.items));
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?\nThis will save the contents of the note.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ExistingIndivNoteActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.pb_indiv_note.setVisibility(View.VISIBLE);
        this.saveNote();
        this.pb_indiv_note.setVisibility(View.GONE);
    }

    private void saveNote() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        this.reference.child((userId)).child(Collection.notes.name())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String noteId = String.valueOf(tvNoteId.getText());
                        System.out.println("Current note id: " + noteId);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        String dateString = formatter.format(date);

                        HashMap<String, Object> noteData = new HashMap<>();

                        title = String.valueOf(tvTitle.getText());
                        subtitle = String.valueOf(tvSubtitle.getText());

                        if(title.equals(""))
                            title = "Title";

                        if(subtitle.equals(""))
                            subtitle = "Subtitle";

                        noteData.put("title", title);
                        noteData.put("subtitle", subtitle);
                        noteData.put("noteType", noteType);
                        noteData.put("dateModified", dateString);
                        noteData.put("tags", tags);

                        System.out.println("title: " + title);
                        System.out.println("subtitle: " + subtitle);
                        System.out.println("noteType: " + noteType);
                        System.out.println("dateModified: " + dateString);
                        System.out.println("tags: " + tags);
                        System.out.println("note id: " + noteId);

                        ArrayList<ArrayList<String>> tempItems = new ArrayList<>();

                        if(noteType.equals("Blank")){
                            EditText tempText;
                            for(int i = 0; i < indivNotesManager.getChildCount(); i++){
                                tempText = indivNotesManager.getChildAt(i).findViewById(R.id.etml_blank_text);
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempText.getText()));
                                tempItems.add(new ArrayList<String>(Arrays.asList(String.valueOf(tempText.getText()))));
                            }
//                            tempItems.add(new ArrayList<String>(Arrays.asList("Hello test note")));
//                            tempItems.add(new ArrayList<String>(Arrays.asList("Hi new blank item")));

                            Log.d("item strings: ", String.valueOf(tempItems));

                            reference.child((userId)).child(Collection.notes.name()).child(noteId).child("blankItems").setValue(tempItems);

                        }
                        else if(noteType.equals("ToDo")){
                            CheckBox tempTodo;
                            TextView tempTodoText;
                            for(int i = 0; i < indivNotesManager.getChildCount(); i++){
                                tempTodo = indivNotesManager.getChildAt(i).findViewById(R.id.cb_todo_checkbox);
                                tempTodoText = indivNotesManager.getChildAt(i).findViewById(R.id.et_todo_text);
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempTodo.isChecked()));
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempTodo.getText()));
                                String str[] = {String.valueOf(tempTodo.isChecked()), String.valueOf(tempTodoText.getText())};
                                tempItems.add(new ArrayList<String>(Arrays.asList(str)));
                            }

                            Log.d("item strings: ", String.valueOf(tempItems));

                            reference.child((userId)).child(Collection.notes.name()).child(noteId).child("todo").setValue(tempItems);

                        }
                        else if(noteType.equals("Interest")){
                            RatingBar tempRatingBar;
                            EditText tempText;
                            TextView tempTitle;
                            for(int i = 0; i < indivNotesManager.getChildCount(); i++){
                                tempRatingBar = indivNotesManager.getChildAt(i).findViewById(R.id.rb_interest_rating);
                                tempText = indivNotesManager.getChildAt(i).findViewById(R.id.etml_interest_text);
                                tempTitle = indivNotesManager.getChildAt(i).findViewById(R.id.etml_interest_title);
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempRatingBar.getRating()));
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempText.getText()));
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempTitle.getText()));
                                String str[] = {"default_image", String.valueOf(tempRatingBar.getRating()),
                                        String.valueOf(tempTitle.getText()), String.valueOf(tempText.getText())};
                                tempItems.add(new ArrayList<String>(Arrays.asList(str)));
                            }

                            Log.d("item strings: ", String.valueOf(tempItems));

                            reference.child((userId)).child(Collection.notes.name()).child(noteId).child("interestItem").setValue(tempItems);

                        }
                        else if(noteType.equals("Detailed")){
                            EditText tempTitle, tempSubtitle, tempText;
                            for(int i = 0; i < indivNotesManager.getChildCount(); i++){
                                tempTitle = indivNotesManager.getChildAt(i).findViewById(R.id.etml_detailed_title);
                                tempSubtitle = indivNotesManager.getChildAt(i).findViewById(R.id.etml_detailed_subtitle);
                                tempText = indivNotesManager.getChildAt(i).findViewById(R.id.etml_detailed_text);
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempTitle.getText()));
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempSubtitle.getText()));
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempText.getText()));
                                String str[] = {"default_image", String.valueOf(tempTitle.getText()),
                                        String.valueOf(tempSubtitle.getText()), String.valueOf(tempText.getText())};
                                tempItems.add(new ArrayList<String>(Arrays.asList(str)));
                            }

                            Log.d("item strings: ", String.valueOf(tempItems));

                            reference.child((userId)).child(Collection.notes.name()).child(noteId).child("interestItem").setValue(tempItems);

                        }
                        else if(noteType.equals("Lesson")){
                            EditText tempTitle, tempSubtitle, tempText;
                            for(int i = 0; i < indivNotesManager.getChildCount(); i++){
                                tempTitle = indivNotesManager.getChildAt(i).findViewById(R.id.et_lesson_title);
                                tempSubtitle = indivNotesManager.getChildAt(i).findViewById(R.id.et_lesson_subtitle);
                                tempText = indivNotesManager.getChildAt(i).findViewById(R.id.et_lesson_text);
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempTitle.getText()));
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempSubtitle.getText()));
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempText.getText()));
                                String str[] = {String.valueOf(tempTitle.getText()),
                                        String.valueOf(tempSubtitle.getText()), String.valueOf(tempText.getText())};
                                tempItems.add(new ArrayList<String>(Arrays.asList(str)));
                            }

                            Log.d("item strings: ", String.valueOf(tempItems));

                            reference.child((userId)).child(Collection.notes.name()).child(noteId).child("lessonNotesItem").setValue(tempItems);

                        }

                        noteData.put("blankItems", tempItems);

                        reference.child((userId)).child(Collection.notes.name()).child(noteId).child("title").setValue(title);
                        reference.child((userId)).child(Collection.notes.name()).child(noteId).child("subtitle").setValue(subtitle);
                        reference.child((userId)).child(Collection.notes.name()).child(noteId).child("dateModified").setValue(dateString);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void TakePicture() {
        Intent cameraPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(cameraPictureIntent, 1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No camera detected!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            note_ib_holder.setImageBitmap((Bitmap) extras.get("data"));
        }
    }



    @Override
    public void callCamera(ImageButton imageButton) {
        note_ib_holder = imageButton;
        TakePicture();
    }
}