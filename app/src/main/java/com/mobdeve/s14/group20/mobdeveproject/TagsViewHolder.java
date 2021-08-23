package com.mobdeve.s14.group20.mobdeveproject;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TagsViewHolder extends RecyclerView.ViewHolder {

    private TextView tvText;

    public TagsViewHolder(@NonNull View itemView) {
        super(itemView);

        this.tvText = itemView.findViewById(R.id.tv_tag_template_text);
    }

    public void setTvText(String tvText){ this.tvText.setText(tvText); }

    public TextView getTvText(){ return this.tvText; }
}
