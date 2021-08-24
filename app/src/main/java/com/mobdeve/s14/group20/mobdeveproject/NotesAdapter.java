package com.mobdeve.s14.group20.mobdeveproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private ArrayList<Note> notes;
    public Context cxt;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

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

        this.sp = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        this.spEditor = this.sp.edit();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        View itemView = holder.getItemView();

        System.out.println("Position: " + position + " | Context: " + itemView.getContext());

        holder.setTvDateModified(notes.get(position).getDateModified());
        holder.setTvTitle(notes.get(position).getTitle());
        holder.setTvSubtitle(notes.get(position).getSubtitle());

        if(notes.get(position).getNoteType().equals("ToDo")){
            holder.setIvLogo(R.drawable.todo_list);
            holder.setViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int i, j;

                    Log.d("EDITOR", String.valueOf(spEditor));
                    Log.d("NAME", Keys.TODO_TITLE.name());
                    Log.d("TITLE", notes.get(position).getTitle());

                    spEditor.putString(Keys.TODO_TITLE.name(), notes.get(position).getTitle());
                    spEditor.putString(Keys.TODO_SUBTITLE.name(), notes.get(position).getSubtitle());
                    spEditor.putString(Keys.TODO_NOTETYPE.name(), notes.get(position).getNoteType());

                    for(i = 0; i < notes.get(position).getToDo().size(); i++){
                        spEditor.putBoolean(Keys.TODO_ITEMS.name() + i + "checkbox", notes.get(position).getToDo().get(i).getIsDone());
                        spEditor.putString(Keys.TODO_ITEMS.name() + i + "string", notes.get(position).getToDo().get(i).getText());
                    }

                    spEditor.putInt(Keys.TODO_ITEMS_LENGTH.name(), i);

                    for(j = 0; i < notes.get(position).getTags().size(); i++){
                        spEditor.putString(Keys.TODO_TAGS.name() + j, notes.get(position).getTags().get(j));
                    }

                    spEditor.putInt(Keys.TODO_TAGS_LENGTH.name(), j);
                    spEditor.apply();

                    Intent intent = new Intent(v.getContext(), IndivNoteActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
        else if(notes.get(position).getNoteType().equals("Sketchbook")){
            holder.setIvLogo(R.drawable.sketchbook);
            holder.setViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(), IndivNoteActivity.class);
                    v.getContext().startActivity(i);
                }
            });
        }
        else if(notes.get(position).getNoteType().equals("Interest")){
            holder.setIvLogo(R.drawable.interest);
            holder.setViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(), IndivNoteActivity.class);
                    v.getContext().startActivity(i);
                }
            });
        }
        else if(notes.get(position).getNoteType().equals("Detailed")){
            holder.setIvLogo(R.drawable.lesson_notes);
            holder.setViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(), IndivNoteActivity.class);
                    v.getContext().startActivity(i);
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
