package com.mobdeve.s14.group20.mobdeveproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IndivNotesAdapter extends RecyclerView.Adapter<IndivNotesAdapter.IndivNotesViewHolder> {

    private ArrayList<String> tags;
    private ArrayList<Boolean> toDoIsDone;
    private ArrayList<String> toDoText;

    public IndivNotesAdapter(ArrayList<String> tags, ArrayList<Boolean> isDone, ArrayList<String> text){

        this.tags = tags;
        this.toDoIsDone = isDone;
        this.toDoText = text;
    }

    @NonNull
    @Override
    public IndivNotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        IndivNotesViewHolder viewHolder = new IndivNotesViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IndivNotesAdapter.IndivNotesViewHolder holder, int position) {

        holder.bindData(toDoIsDone.get(position), toDoText.get(position));
    }

    @Override
    public int getItemCount() {
        return this.toDoText.size();
    }

    public class IndivNotesViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cbItem;

        public IndivNotesViewHolder(@NonNull View itemView) {
            super(itemView);

            cbItem = itemView.findViewById(R.id.cb_todo_checkbox);
        }

        public CheckBox getCbItem(){ return this.cbItem; }

        public void bindData(boolean check, String text){
            this.cbItem.setChecked(check);
            this.cbItem.setText(text);
        }

        public void setCbItemChecked(boolean check){ this.cbItem.setChecked(check); }

        public void setCbItemText(String text){ this.cbItem.setText(text); }
    }
}
