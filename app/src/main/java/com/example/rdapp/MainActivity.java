package com.example.rdapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    RecyclerView goalList;
    GoalsAdapter goalsAdapter;

    LinkedList<Goal> goals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!retrieveGoals()) {
            goals = new LinkedList<>();
        }

        goalList = findViewById(R.id.rvGoals);

        goalsAdapter = new GoalsAdapter(this, goals);
        goalList.setAdapter(goalsAdapter);
        goalList.setLayoutManager(new LinearLayoutManager(this));

//        File directory = getFilesDir();
//        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(go)
//        if (json != null) {
//            goals = com.google.gson.internal.fromJson(json, LinkedList.class);
//        }

        Intent intent = getIntent();
        if (intent.hasExtra(AddGoalActivity.GOAL_TITLE) && intent.hasExtra((AddGoalActivity.GOAL_DESCRIPTION)) && intent.hasExtra(AddGoalActivity.GOAL_DEADLINE)) {
            goals.add(new Goal(intent.getStringExtra(AddGoalActivity.GOAL_TITLE), intent.getStringExtra(AddGoalActivity.GOAL_DESCRIPTION), intent.getStringExtra(AddGoalActivity.GOAL_DEADLINE)));
            goalsAdapter.notifyDataSetChanged();
        }

        saveGoals();
    }

    private boolean retrieveGoals() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String json = sharedPreferences.getString("goals", "");
        if (json == "") {
            return false;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<LinkedList<Goal>>(){}.getType();
        goals = gson.fromJson(json, type);
        System.out.println("retrieved goals");
        return true;
    }

    private void saveGoals() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(goals);
        prefsEditor.putString("goals", json);
        prefsEditor.commit();
        System.out.println("Saved goals");
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