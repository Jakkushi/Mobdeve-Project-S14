package com.mobdeve.s14.group20.mobdeveproject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class NotesViewHolder extends RecyclerView.ViewHolder{

    private TextView tvTitle, tvSubtitle, tvDateModified, tvNoteId;
    private ImageView ivLogo, ivBackground;
    private ImageButton ibDelete;
    private RecyclerView rvTags;
    private ConstraintLayout clTemplate;
    public View itemView;
    public Context context;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        this.tvTitle = itemView.findViewById(R.id.tv_note_template_title);
        this.tvSubtitle = itemView.findViewById(R.id.tv_note_template_subtitle);
        this.tvDateModified = itemView.findViewById(R.id.tv_note_template_date);
        this.ivLogo = itemView.findViewById(R.id.iv_note_template_logo);
        this.rvTags = itemView.findViewById(R.id.rv_note_template_tags);
        this.ivBackground = itemView.findViewById(R.id.ib_note_template_background);
        this.ibDelete = itemView.findViewById(R.id.ib_note_template_delete);
        this.tvNoteId = itemView.findViewById(R.id.tv_note_template_id);

        this.context = itemView.getContext();
        this.itemView = itemView;

        System.out.println("INSIDE VH" + itemView.getContext());
    }

    public void setTvTitle(String tvTitle){ this.tvTitle.setText(tvTitle); }

    public void setTvSubtitle(String tvSubtitle){ this.tvSubtitle.setText(tvSubtitle); }

    public void setTvDateModified(String tvDateModified){ this.tvDateModified.setText(tvDateModified); }

    public void setIvLogo(int ivLogo){ this.ivLogo.setImageResource(ivLogo); }

    public void setViewOnClickListener(View.OnClickListener onClickListener){
        this.ivBackground.setOnClickListener(onClickListener);
    }

    public TextView getTvTitle(){ return this.tvTitle; }

    public TextView getTvSubtitle(){ return this.tvSubtitle; }

    public TextView getTvDateModified(){ return this.tvDateModified; }

    public ImageView getIvLogo(){ return this.ivLogo; }

    public TextView getTvNoteId(){ return this.tvNoteId; }

    public View getItemView(){ return this.itemView; }

    public RecyclerView getRvTags(){ return this.rvTags; }

    public ImageButton getIbDelete() {
        return this.ibDelete;
    }

    public void setTvNoteId(String noteId){
        this.tvNoteId.setText(noteId);
    }

    public void setIbDelete(ImageButton ibDelete) {
        this.ibDelete = ibDelete;
    }
}
