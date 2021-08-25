package com.mobdeve.s14.group20.mobdeveproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class IndivTagsAdapter extends RecyclerView.Adapter<IndivTagsAdapter.IndivTagsViewHolder> {

    private ArrayList<String> tags;

    public IndivTagsAdapter(ArrayList<String> tags){
        this.tags = tags;
    }

    @NonNull
    @NotNull
    @Override
    public IndivTagsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.tag_template, parent, false);
        IndivTagsViewHolder viewHolder = new IndivTagsViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull IndivTagsViewHolder holder, int position) {

        holder.bindTag(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return this.tags.size();
    }

    public class IndivTagsViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTag;

        public IndivTagsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvTag = itemView.findViewById(R.id.tv_tag_template_text);
        }

        public void bindTag(String tag){

            Log.d("TVTAG", String.valueOf(tvTag));
            tvTag.setText(tag);
        }
    }
}
