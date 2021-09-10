package com.mobdeve.s14.group20.mobdeveproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private ArrayList<Note> notes;
    public Context cxt;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private String userId;
    private FirebaseUser user;
    boolean withChild;

    public NotesAdapter(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public Context getContext(){ return this.cxt; }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.note_template, parent, false);

        NotesViewHolder viewHolder = new NotesViewHolder(itemView);

        this.cxt = parent.getContext();

        Log.d("In notes adapter: ", String.valueOf(parent.getContext()));

        this.initFirebase();

        return viewHolder;
    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.user = mAuth.getCurrentUser();
        this.userId = user.getUid();
        this.reference = this.database.getReference().child(Collection.users.name()).child((this.userId)).child(Collection.notes.name());
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        View itemView = holder.getItemView();

        System.out.println("Position: " + position + " | Context: " + itemView.getContext());

        holder.setTvDateModified(notes.get(position).getDateModified());
        holder.setTvTitle(notes.get(position).getTitle());
        holder.setTvSubtitle(notes.get(position).getSubtitle());
        holder.getTvNoteId().setText(notes.get(position).getNoteId());

        holder.getIbDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to delete this note?\nDeleted notes can no longer be retrieved after the action")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                withChild = false;
                                Log.d("DELETING THIS NOTE ID: ", String.valueOf(reference.child(notes.get(position).getNoteId())));
                                reference.child(notes.get(position).getNoteId()).removeValue();

                                ArrayList<DatabaseNotesData> dbNotes = new ArrayList<DatabaseNotesData>();

                                reference.orderByChild(Collection.dateModified.name()).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                        withChild = true;

                                        ArrayList<ArrayList<String>> interestItems = new ArrayList<>();
                                        ArrayList<ArrayList<String>> lessonItems = new ArrayList<>();
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

                                        try {
                                            lessonItems = (ArrayList) (((HashMap) snapshot.getValue()).get("lessonNotesItem"));
                                        }
                                        catch (Exception d){
                                            tags = null;
                                            Log.w("error", "No Lesson items in entry");
                                        }

                                        Log.d("Snapshot Key: ", snapshot.getKey());

                                        dbNotes.add(new DatabaseNotesData( (String) (( (HashMap) snapshot.getValue()).get("title")),
                                                (String) ((HashMap) snapshot.getValue()).get("subtitle"), (String) ((HashMap) snapshot.getValue()).get("noteType"),
                                                (String) ((HashMap) snapshot.getValue()).get("dateModified"), interestItems, tags, todoList, blankItems, lessonItems, snapshot.getKey()));

                                        Intent intent = new Intent(cxt, DisplayNotesActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra(Keys.DBNOTES.name(), dbNotes);
                                        cxt.startActivity(intent);
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

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if(!withChild){
                                            Intent intent = new Intent(cxt, DisplayNotesActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra(Keys.DBNOTES.name(), dbNotes);
                                            cxt.startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        if(notes.get(position).getNoteType().equals("ToDo")){
            holder.setIvLogo(R.drawable.todo_list);
            holder.setViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), ExistingIndivNoteActivity.class);
                    intent.putExtra(Keys.TITLE.name(), notes.get(position).getTitle());
                    intent.putExtra(Keys.SUBTITLE.name(), notes.get(position).getSubtitle());
                    intent.putExtra(Keys.NOTETYPE.name(), notes.get(position).getNoteType());
                    intent.putExtra(Keys.ITEMS.name(), notes.get(position).getItems());
                    intent.putExtra(Keys.TAGS.name(), notes.get(position).getTags());
                    intent.putExtra(Keys.ID.name(), notes.get(position).getNoteId());
                    v.getContext().startActivity(intent);
                }
            });
        }
        else if(notes.get(position).getNoteType().equals("Sketchbook")){
            holder.setIvLogo(R.drawable.sketchbook);
            holder.setViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), SketchActivity.class);
                    intent.putExtra(Keys.TITLE.name(), notes.get(position).getTitle());
                    intent.putExtra(Keys.SUBTITLE.name(), notes.get(position).getSubtitle());
                    intent.putExtra(Keys.NOTETYPE.name(), notes.get(position).getNoteType());
                    intent.putExtra(Keys.TAGS.name(), notes.get(position).getTags());
                    intent.putExtra(Keys.ID.name(), notes.get(position).getNoteId());
                    v.getContext().startActivity(intent);
                }
            });
        }
        else if(notes.get(position).getNoteType().equals("Interest")){
            holder.setIvLogo(R.drawable.interest);
            holder.setViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), ExistingIndivNoteActivity.class);
                    intent.putExtra(Keys.TITLE.name(), notes.get(position).getTitle());
                    intent.putExtra(Keys.SUBTITLE.name(), notes.get(position).getSubtitle());
                    intent.putExtra(Keys.NOTETYPE.name(), notes.get(position).getNoteType());
                    intent.putExtra(Keys.ITEMS.name(), notes.get(position).getItems());
                    intent.putExtra(Keys.TAGS.name(), notes.get(position).getTags());
                    intent.putExtra(Keys.ID.name(), notes.get(position).getNoteId());
                    v.getContext().startActivity(intent);
                }
            });
        }
        else if(notes.get(position).getNoteType().equals("Detailed")){
            holder.setIvLogo(R.drawable.detailed);
            holder.setViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), ExistingIndivNoteActivity.class);
                    intent.putExtra(Keys.TITLE.name(), notes.get(position).getTitle());
                    intent.putExtra(Keys.SUBTITLE.name(), notes.get(position).getSubtitle());
                    intent.putExtra(Keys.NOTETYPE.name(), notes.get(position).getNoteType());
                    intent.putExtra(Keys.ITEMS.name(), notes.get(position).getItems());
                    intent.putExtra(Keys.TAGS.name(), notes.get(position).getTags());
                    intent.putExtra(Keys.ID.name(), notes.get(position).getNoteId());
                    v.getContext().startActivity(intent);
                }
            });
        }
        else if(notes.get(position).getNoteType().equals("Blank")){
            holder.setIvLogo(R.drawable.blank_page);
            holder.setViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), ExistingIndivNoteActivity.class);
                    intent.putExtra(Keys.TITLE.name(), notes.get(position).getTitle());
                    intent.putExtra(Keys.SUBTITLE.name(), notes.get(position).getSubtitle());
                    intent.putExtra(Keys.NOTETYPE.name(), notes.get(position).getNoteType());
                    intent.putExtra(Keys.ITEMS.name(), notes.get(position).getBlankItems());
                    intent.putExtra(Keys.TAGS.name(), notes.get(position).getTags());
                    intent.putExtra(Keys.ID.name(), notes.get(position).getNoteId());
                    v.getContext().startActivity(intent);
                }
            });
        }
        else if(notes.get(position).getNoteType().equals("Lesson")){
            holder.setIvLogo(R.drawable.lesson_notes);
            holder.setViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), ExistingIndivNoteActivity.class);
                    intent.putExtra(Keys.TITLE.name(), notes.get(position).getTitle());
                    intent.putExtra(Keys.SUBTITLE.name(), notes.get(position).getSubtitle());
                    intent.putExtra(Keys.NOTETYPE.name(), notes.get(position).getNoteType());
                    intent.putExtra(Keys.ITEMS.name(), notes.get(position).getItems());
                    intent.putExtra(Keys.TAGS.name(), notes.get(position).getTags());
                    intent.putExtra(Keys.ID.name(), notes.get(position).getNoteId());
                    v.getContext().startActivity(intent);
                }
            });
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.cxt, LinearLayoutManager.HORIZONTAL, false);
        holder.getRvTags().setLayoutManager(layoutManager);

        TagsAdapter tagAdapter = new TagsAdapter(notes.get(position).getTags());
        holder.getRvTags().setAdapter(tagAdapter);
    }

    @Override
    public int getItemCount() {
        return this.notes.size();
    }
}
