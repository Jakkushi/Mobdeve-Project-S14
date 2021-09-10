package com.mobdeve.s14.group20.mobdeveproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IndivNoteActivity extends AppCompatActivity implements IndivNotesAdapter.callAction {

    private TextView tvTitle, tvSubtitle;
    private RecyclerView rvIndivNotes, rvIndivTags;
    private RecyclerView.LayoutManager indivNotesManager, indivTagsManager;
    private IndivNotesAdapter indivNotesAdapter;
    private IndivTagsAdapter indivTagsAdapter;
    private ImageButton note_ib_holder;
    private FloatingActionButton fabAddTemplate;

    private String title, subtitle, noteType;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private static int REQUEST_CODE_CAMERA = 1;
    private static int REQUEST_CODE_IMAGE_SELECT = 2;

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
        this.fabAddTemplate = findViewById(R.id.indiv_fab_add);
        bindFabOnClick();

        this.tvTitle.setText(this.title);
        this.tvSubtitle.setText(this.subtitle);
    }

    private static final String default_url = "https://firebasestorage.googleapis.com/v0/b/tous-les-journal.appspot.com/o/default_image.png?alt=media&token=7db691ef-1bef-46fb-98b6-9c4c445b3747";

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
                    Item newItem = new InterestItem(default_url, 0, "", "", IndivNoteActivity.this);
                    items.add(newItem);
                    indivNotesAdapter.notifyItemInserted(items.size() - 1);
                }
                else if(noteType.equals("Detailed")){
                    Item newItem = new DetailedItem(default_url, "", "", "", IndivNoteActivity.this);
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
        this.indivNotesAdapter = new IndivNotesAdapter(this.title, this.tags, this.items, this);
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


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?\nThis will save the contents of the note.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        IndivNoteActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.saveNote();
    }

    private void saveNote() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        this.reference.child((userId)).child(Collection.notes.name())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //check for note id???
                        String newNoteId = reference.push().getKey();
                        System.out.println("New note id: " + newNoteId);

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

                        ArrayList<ArrayList<String>> tempItems = new ArrayList<>();

                        if(noteType.equals("Blank")){
                            TextView tempText;
                            for(int i = 0; i < indivNotesManager.getChildCount(); i++){
                                tempText = indivNotesManager.getChildAt(i).findViewById(R.id.etml_blank_text);
                                Log.d("CHILD: ", i + ": " + String.valueOf(tempText.getText()));
                                tempItems.add(new ArrayList<String>(Arrays.asList(String.valueOf(tempText.getText()))));
                            }
//                            tempItems.add(new ArrayList<String>(Arrays.asList("Hello test note")));
//                            tempItems.add(new ArrayList<String>(Arrays.asList("Hi new blank item")));

                            Log.d("item strings: ", String.valueOf(tempItems));
                            noteData.put("blankItems", tempItems);
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

                                Log.d("Inside ARRAY", str[0] + str[1] + str[2]);
                                tempItems.add(new ArrayList<String>(Arrays.asList(str)));
                            }

                            Log.d("item strings: ", String.valueOf(tempItems));
                            noteData.put("lessonNotesItem", tempItems);
                        }
                        else if(noteType.equals("ToDo")){
                            CheckBox tempCheck;
                            EditText tempText;
                            for(int i = 0; i < indivNotesManager.getChildCount(); i++){
                                tempCheck = indivNotesManager.getChildAt(i).findViewById(R.id.cb_todo_checkbox);
                                tempText = indivNotesManager.getChildAt(i).findViewById(R.id.et_todo_text);
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempTitle.getText()));
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempSubtitle.getText()));
//                                Log.d("CHILD: ", i + ": " + String.valueOf(tempText.getText()));
                                String str[] = {String.valueOf(tempCheck.isChecked()), String.valueOf(tempText.getText())};

                                Log.d("Inside ARRAY", str[0] + str[1]);
                                tempItems.add(new ArrayList<String>(Arrays.asList(str)));
                            }

                            Log.d("item strings: ", String.valueOf(tempItems));
                            noteData.put("todo", tempItems);
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
                            noteData.put("interestItem", tempItems);
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
                            noteData.put("interestItem", tempItems);
                        }

                        reference.child((userId)).child(Collection.notes.name()).child(newNoteId).setValue(noteData);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void takePicture() {
        Intent cameraPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(cameraPictureIntent, REQUEST_CODE_CAMERA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No camera detected!", Toast.LENGTH_LONG).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            note_ib_holder.setImageBitmap((Bitmap) extras.get("data"));
        } else if (requestCode == REQUEST_CODE_IMAGE_SELECT && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        note_ib_holder.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void callAction(ImageButton imageButton, TextView textView) {
        note_ib_holder = imageButton;
        String[] options = {"Take a picture from camera", "Select from gallery", "Cancel"};

        new AlertDialog.Builder(this)
                .setTitle("Choose an Action")
                .setItems(options, (dialog, which) -> {
                    switch(which) {
                        case 0:
                            takePicture();
                            break;
                        case 1:
                            openGallery();
                            break;
                    }
                }).show();
    }
}