package com.mobdeve.s14.group20.mobdeveproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private ArrayList<Note> notes;
    public Context cxt;

    public NotesAdapter(ArrayList<Note> notes) { this.notes = notes; }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        System.out.println("INSIDE NOTES" + parent.getContext());
        View itemView = inflater.inflate(R.layout.note_template, parent, false);

        NotesViewHolder viewHolder = new NotesViewHolder(itemView);

        this.cxt = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.setTvDateModified(notes.get(position).getDateModified());
        holder.setTvTitle(notes.get(position).getTitle());
        holder.setTvSubtitle(notes.get(position).getSubtitle());

        if(notes.get(position).getNoteType().equals("Todo")){
            holder.setIvLogo(R.drawable.todo_list);
        }
        else if(notes.get(position).getNoteType().equals("Sketchbook")){
            holder.setIvLogo(R.drawable.sketchbook);
        }
        else if(notes.get(position).getNoteType().equals("Interest")){
            holder.setIvLogo(R.drawable.interest);
        }
        else if(notes.get(position).getNoteType().equals("Detailed")){
            holder.setIvLogo(R.drawable.lesson_notes);
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
