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

                    Intent intent = new Intent(v.getContext(), ExistingIndivNoteActivity.class);
                    intent.putExtra(Keys.TITLE.name(), notes.get(position).getTitle());
                    intent.putExtra(Keys.SUBTITLE.name(), notes.get(position).getSubtitle());
                    intent.putExtra(Keys.NOTETYPE.name(), notes.get(position).getNoteType());
                    intent.putExtra(Keys.ITEMS.name(), notes.get(position).getItems());
                    intent.putExtra(Keys.TAGS.name(), notes.get(position).getTags());
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
                    v.getContext().startActivity(intent);
                }
            });
        }
        else if(notes.get(position).getNoteType().equals("Detailed")){
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
                    intent.putExtra(Keys.ITEMS.name(), notes.get(position).getItems());
                    intent.putExtra(Keys.TAGS.name(), notes.get(position).getTags());
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
