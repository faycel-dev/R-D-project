package com.example.rdapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder> {
    Context context;
    LinkedList<Goal> goals;

    public GoalsAdapter(Context context, LinkedList<Goal> goals) {
        this.context = context;
        this.goals = goals;
    }

    @NonNull
    @Override
    public GoalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_goal, parent, false);
        return new GoalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalsViewHolder holder, int position) {
        holder.tvGoalTitle.setText(goals.get(position).getTitle());
        holder.tvGoalDescription.setText(goals.get(position).getDescription());
        holder.tvGoalDeadline.setText(goals.get(position).getDeadline());

        holder.itemView.setTag(goals.get(position));
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public class GoalsViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvGoalTitle, tvGoalDescription, tvGoalDeadline;

        public GoalsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvGoalTitle = itemView.findViewById(R.id.tvGoalTitle);
            tvGoalDescription = itemView.findViewById(R.id.tvGoalDescription);
            tvGoalDeadline = itemView.findViewById(R.id.tvGoalDeadline);
        }
    }
}
