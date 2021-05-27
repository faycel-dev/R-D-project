package com.example.rdapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    RecyclerView goalList;
    GoalsAdapter goalsAdapter;

    LinkedList<Goal> goals;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!retrieveGoals()) {
            goals = new LinkedList<>();
        }

        goalList = findViewById(R.id.rvGoals);

        Intent intent = getIntent();
        if (intent.hasExtra(AddGoalActivity.GOAL_TITLE) && intent.hasExtra((AddGoalActivity.GOAL_DESCRIPTION)) && intent.hasExtra(AddGoalActivity.GOAL_DEADLINE)) {
            if (intent.hasExtra(EditGoalActivity.EDIT_GOAL) && intent.hasExtra(EditGoalActivity.GOAL_INDEX)) {
                int goalIndex = intent.getIntExtra(EditGoalActivity.GOAL_INDEX, 0);

                Goal goal = goals.get(goalIndex);
                goal.setDeadline(intent.getStringExtra(EditGoalActivity.GOAL_DEADLINE));
                goal.setDescription(intent.getStringExtra(EditGoalActivity.GOAL_DESCRIPTION));
                goal.setTitle(intent.getStringExtra(EditGoalActivity.GOAL_TITLE));

                System.out.println(goalIndex);

                goals.set(goalIndex, goal);
            } else {
                goals.addLast(new Goal(intent.getStringExtra(AddGoalActivity.GOAL_TITLE), intent.getStringExtra(AddGoalActivity.GOAL_DESCRIPTION), intent.getStringExtra(AddGoalActivity.GOAL_DEADLINE)));
                goalsAdapter.notifyDataSetChanged();
                db = FirebaseFirestore.getInstance();
                addDataFireStore();
            }
        }

        for (Goal g : goals) {
            System.out.println(g.getTitle());
        }

        goalsAdapter = new GoalsAdapter(this, goals);
        goalList.setAdapter(goalsAdapter);
        goalList.setLayoutManager(new LinearLayoutManager(this));

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

    public void onTapGoal(View view) {
        Intent intent = new Intent(this, EditGoalActivity.class);
        //intent.putExtra(EditGoalActivity.GOAL_INDEX, );
        intent.putExtra(EditGoalActivity.GOAL_TITLE, ((TextView) view.findViewById(R.id.tvGoalTitle)).getText().toString());
        intent.putExtra(EditGoalActivity.GOAL_DESCRIPTION, ((TextView) view.findViewById(R.id.tvGoalDescription)).getText().toString());
        intent.putExtra(EditGoalActivity.GOAL_DEADLINE, ((TextView) view.findViewById(R.id.tvGoalDeadline)).getText().toString());
        startActivity(intent);
    }

    public void addDataFireStore(){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        DocumentReference dbDocument = db.document("users/" + userId);
        HashMap<String, Object> Goals = new HashMap<>();
        if(goals.size() >0) {
            Goals.put(String.valueOf(goals.size()-1), goals.getLast());
            System.out.println(dbDocument.getId());
            dbDocument.set(Goals, SetOptions.merge())/*.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(MainActivity.this, "Your goals have been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Fail to add goals \n" + e, Toast.LENGTH_SHORT).show();
                }
            })*/;
        }
    }
}