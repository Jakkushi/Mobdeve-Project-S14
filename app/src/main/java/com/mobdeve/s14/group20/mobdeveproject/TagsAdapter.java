package com.mobdeve.s14.group20.mobdeveproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TagsAdapter extends RecyclerView.Adapter<TagsViewHolder> {

    private ArrayList<String> tags;

    public TagsAdapter(ArrayList<String> tags){ this.tags = tags; }

    @NonNull
    @Override
    public TagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.tag_template, parent, false);

        TagsViewHolder viewHolder = new TagsViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TagsViewHolder holder, int position) {

        holder.setTvText(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return this.tags.size();
    }
}
