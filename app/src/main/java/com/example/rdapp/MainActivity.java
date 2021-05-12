package com.example.rdapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    RecyclerView goalList;
    GoalsAdapter goalsAdapter;

    LinkedList<Goal> goals = new LinkedList<Goal>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goalList = findViewById(R.id.rvGoals);

        goalsAdapter = new GoalsAdapter(this, goals);
        goalList.setAdapter(goalsAdapter);
        goalList.setLayoutManager(new LinearLayoutManager(this));

        goals.add(new Goal("Title", "Description", "Deadline"));

        Intent intent = getIntent();
        if (intent.hasExtra(AddGoalActivity.GOAL_TITLE) && intent.hasExtra((AddGoalActivity.GOAL_DESCRIPTION)) && intent.hasExtra(AddGoalActivity.GOAL_DEADLINE)) {
            goals.add(new Goal(intent.getStringExtra(AddGoalActivity.GOAL_TITLE), intent.getStringExtra(AddGoalActivity.GOAL_DESCRIPTION), intent.getStringExtra(AddGoalActivity.GOAL_DEADLINE)));
            goalsAdapter.notifyDataSetChanged();
        }

        System.out.println(goals);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    public void onAddGoal(View view) {
        Intent intent = new Intent(this, AddGoalActivity.class);
        startActivity(intent);
    }
}