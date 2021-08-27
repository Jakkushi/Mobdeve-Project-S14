package com.mobdeve.s14.group20.mobdeveproject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IndivNotesAdapter extends RecyclerView.Adapter<IndivNotesAdapter.IndivNotesViewHolder> {

    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private String title;
    private callCamera mListener;

    public IndivNotesAdapter(String title, ArrayList<String> tags, ArrayList<Item> items, callCamera mListener) {

        this.title = title;
        this.tags = tags;
        this.items = items;
        this.mListener = mListener;

    }

    @NonNull
    @Override
    public IndivNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(this.items.get(0) instanceof ToDoItem){
            View itemView = inflater.inflate(R.layout.note_todo, parent, false);
            IndivNotesViewHolder viewHolder = new IndivNotesViewHolder(itemView);
            return viewHolder;
        }
        else if(this.items.get(0) instanceof InterestItem){
            View itemView = inflater.inflate(R.layout.note_interest_tracker, parent, false);
            IndivNotesViewHolder viewHolder = new IndivNotesViewHolder(itemView);
            return viewHolder;
        }
        else if(this.items.get(0) instanceof DetailedItem){
            View itemView = inflater.inflate(R.layout.note_detailed_list, parent, false);
            IndivNotesViewHolder viewHolder = new IndivNotesViewHolder(itemView);
            return viewHolder;
        }
        else if(this.items.get(0) instanceof BlankItem){
            View itemView = inflater.inflate(R.layout.note_blank, parent, false);
            IndivNotesViewHolder viewHolder = new IndivNotesViewHolder(itemView);
            return viewHolder;
        }
        else if(this.items.get(0) instanceof LessonNotesItem){
            View itemView = inflater.inflate(R.layout.note_lesson_notes, parent, false);
            IndivNotesViewHolder viewHolder = new IndivNotesViewHolder(itemView);
            return viewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull IndivNotesAdapter.IndivNotesViewHolder holder, int position) {
        if(items.get(position) instanceof ToDoItem){
            holder.bindToDo((ToDoItem) items.get(position));
        }
        else if(items.get(position) instanceof InterestItem){
            holder.bindInterest((InterestItem) items.get(position));
        }
        else if(items.get(position) instanceof DetailedItem){
            holder.bindDetailed((DetailedItem) items.get(position));
        }
        else if(items.get(position) instanceof BlankItem){
            holder.bindBlank((BlankItem) items.get(position));
        }
        else if(items.get(position) instanceof LessonNotesItem){
            holder.bindLesson((LessonNotesItem) items.get(position));
        }
    }

    @Override
    public int getItemCount() {

        if(this.items == null)
            return 0;
        else return this.items.size();
    }

    public class IndivNotesViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cbItem;
        private TextView interestTitle, blankText;
        private EditText interestText, detailedTitle, detailedSubtitle, detailedText;
        private RatingBar interestRating;
        private ImageButton interestPicture, detailedPicture;
        private EditText lessonTitle, lessonSubtitle, lessonText;

        public IndivNotesViewHolder(@NonNull View itemView) {
            super(itemView);

            cbItem = itemView.findViewById(R.id.cb_todo_checkbox);
            interestTitle = itemView.findViewById(R.id.tv_interest_title);
            interestText = itemView.findViewById(R.id.etml_interest_text);
            interestRating = itemView.findViewById(R.id.rb_interest_rating);
            interestPicture = itemView.findViewById(R.id.ib_interest_image);
            detailedTitle = itemView.findViewById(R.id.etml_detailed_title);
            detailedSubtitle = itemView.findViewById(R.id.etml_detailed_subtitle);
            detailedText = itemView.findViewById(R.id.etml_detailed_text);
            detailedPicture = itemView.findViewById(R.id.ib_detailed_image);
            blankText = itemView.findViewById(R.id.etml_blank_text);
            lessonTitle = itemView.findViewById(R.id.et_lesson_title);
            lessonSubtitle = itemView.findViewById(R.id.et_lesson_subtitle);
            lessonText = itemView.findViewById(R.id.et_lesson_text);
        }

        public void bindToDo(ToDoItem item) {

            this.cbItem.setChecked(item.getIsDone());
            this.cbItem.setText(item.getText());
        }

        public void bindInterest(InterestItem item){
            this.interestTitle.setText(item.getTitle());
            this.interestText.setText(item.getText());
            this.interestRating.setRating(item.getRating());
            this.interestPicture.setImageResource(item.getImgId());
            this.interestPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    mListener.callCamera(detailedPicture);
                }
            });
        }

        public void bindDetailed(DetailedItem item){

            this.detailedPicture.setImageResource(item.getImgId());
            this.detailedTitle.setText(item.getTitle());
            this.detailedSubtitle.setText(item.getSubtitle());
            this.detailedText.setText(item.getText());
            this.detailedPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    mListener.callCamera(detailedPicture);
                }
            });
        }

        public void bindBlank(BlankItem item){

           this.blankText.setText(item.getText());
        }

        public void bindLesson(LessonNotesItem item){

            this.lessonTitle.setText(item.getTitle());
            this.lessonSubtitle.setText(item.getSubtitle());
            this.lessonText.setText(item.getText());
        }
    }

    public interface callCamera {
        void callCamera(ImageButton imageButton);
    }
}
