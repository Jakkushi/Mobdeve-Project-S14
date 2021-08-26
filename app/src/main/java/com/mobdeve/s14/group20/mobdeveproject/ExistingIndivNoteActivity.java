package com.mobdeve.s14.group20.mobdeveproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ExistingIndivNoteActivity extends AppCompatActivity {

    private TextView tvTitle, tvSubtitle;
    private RecyclerView rvIndivNotes, rvIndivTags;
    private RecyclerView.LayoutManager indivNotesManager, indivTagsManager;
    private IndivNotesAdapter indivNotesAdapter;
    private IndivTagsAdapter indivTagsAdapter;
    private TextView tvNoteId;
    private ProgressBar pb_indiv_note;

    private String title, subtitle, noteType, noteId;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indiv_note);

        this.loadData();
        this.bindEssentials();
        this.initRecyclerView();
        this.initFirebase();
    }

    private void initFirebase() {
        this.database = FirebaseDatabase.getInstance();
        this.reference = this.database.getReference().child(Collections.users.name());
    }

    private void bindEssentials(){

        this.tvTitle = findViewById(R.id.et_indiv_title);
        this.tvSubtitle = findViewById(R.id.et_indiv_subtitle);
        this.tvNoteId = findViewById(R.id.tv_note_id);
        this.pb_indiv_note = findViewById(R.id.pb_indiv_note);

        this.tvTitle.setText(this.title);
        this.tvSubtitle.setText(this.subtitle);
        this.tvNoteId.setText(noteId);

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
        this.noteId = getIntent().getStringExtra(Keys.ID.name());

        Log.d("TITLE", this.title);
        Log.d("SUBTITLE", this.subtitle);
        Log.d("NOTETYPE", this.noteType);
        Log.d("TAGS", String.valueOf(this.tags));
        Log.d("TODOITEMS", String.valueOf(this.items));
//        Log.d("NOTEID", String.valueOf(this.noteId));
    }

//    private long lastBackPressTime = 0;
//    private Toast toast;
//
//    @Override
//    public void onBackPressed() {
//        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
//            toast = Toast.makeText(this, "Press back again to close this note. Changes will be saved upon exit.", Toast.LENGTH_LONG);
//            toast.show();
//            this.lastBackPressTime = System.currentTimeMillis();
//        } else {
//            if (toast != null) {
//                toast.cancel();
//            }
//            super.onBackPressed();
//        }
//    }

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

        this.reference.child((userId)).child(Collections.notes.name())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String str[] = (String[])(((HashMap) snapshot.getValue()).keySet().toArray(new String[0]));
//                        ArrayList<String> test = new ArrayList<>(Arrays.asList(str)); //don't forget to cast
//
//                        int numTest = test.size();
//                        for(int i = 0; i < numTest; i++){
//                            System.out.println("item " + i + " : " + test.get(i));
//                        }

                        //check for note id???
                        String noteId = String.valueOf(tvNoteId.getText());
                        System.out.println("Current note id: " + noteId);

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
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
                            tempItems.add(new ArrayList<String>(Arrays.asList("Hello test note")));
                            tempItems.add(new ArrayList<String>(Arrays.asList("Hi new blank item")));

                            Log.d("item strings: ", String.valueOf(tempItems));

                        }

                        noteData.put("blankItems", tempItems);

                        reference.child((userId)).child(Collections.notes.name()).child(noteId).child("title").setValue(title);
                        reference.child((userId)).child(Collections.notes.name()).child(noteId).child("subtitle").setValue(subtitle);
//                        reference.child((userId)).child(Collections.notes.name()).child(noteId).setValue(noteData);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}