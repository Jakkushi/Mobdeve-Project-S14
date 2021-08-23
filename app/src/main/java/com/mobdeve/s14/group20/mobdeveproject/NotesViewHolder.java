package com.mobdeve.s14.group20.mobdeveproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesViewHolder extends RecyclerView.ViewHolder{

    private TextView tvTitle, tvSubtitle, tvDateModified;
    private ImageView ivLogo;
    private RecyclerView rvTags;

    private ArrayList<String> tags;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        this.tvTitle = itemView.findViewById(R.id.tv_note_template_title);
        this.tvSubtitle = itemView.findViewById(R.id.tv_note_template_subtitle);
        this.tvDateModified = itemView.findViewById(R.id.tv_note_template_date);
        this.ivLogo = itemView.findViewById(R.id.iv_note_template_logo);
        this.rvTags = itemView.findViewById(R.id.rv_note_template_tags);

        System.out.println("INSIDE VH" + itemView.getContext());
    }

    public void setTvTitle(String tvTitle){ this.tvTitle.setText(tvTitle); }

    public void setTvSubtitle(String tvSubtitle){ this.tvSubtitle.setText(tvSubtitle); }

    public void setTvDateModified(String tvDateModified){ this.tvDateModified.setText(tvDateModified); }

    public void setIvLogo(int ivLogo){ this.ivLogo.setImageResource(ivLogo); }

    public TextView getTvTitle(){ return this.tvTitle; }

    public TextView getTvSubtitle(){ return this.tvSubtitle; }

    public TextView getTvDateModified(){ return this.tvDateModified; }

    public ImageView getIvLogo(){ return this.ivLogo; }

    public RecyclerView getRvTags(){ return this.rvTags; }
}
