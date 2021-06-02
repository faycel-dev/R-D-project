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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView goalList;
    GoalsAdapter goalsAdapter;

    LinkedList<Goal> goals = new LinkedList<>();

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if (!retrieveGoals()) {
          //  goals = new LinkedList<>();
        //}

        goalList = findViewById(R.id.rvGoals);

        goalsAdapter = new GoalsAdapter(this, goals);
        goalList.setAdapter(goalsAdapter);
        goalList.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent.hasExtra(AddGoalActivity.GOAL_TITLE) && intent.hasExtra((AddGoalActivity.GOAL_DESCRIPTION)) && intent.hasExtra(AddGoalActivity.GOAL_DEADLINE)) {
            if (intent.hasExtra(EditGoalActivity.EDIT_GOAL)) {
                int goalIndex = intent.getIntExtra(EditGoalActivity.GOAL_INDEX, 0);

                Goal goal = (Goal)intent.getSerializableExtra(EditGoalActivity.GOAL);

                System.out.println("------------- ID AFTER EDIT: " + goal.getId());

                String newTitle = intent.getStringExtra(EditGoalActivity.GOAL_TITLE);
                String newDescription = intent.getStringExtra(EditGoalActivity.GOAL_DESCRIPTION);
                String newDeadline = intent.getStringExtra(EditGoalActivity.GOAL_DEADLINE);
                db = FirebaseFirestore.getInstance();
                updateGoals(goal, newTitle, newDescription, newDeadline);

            } else {
                goals.addLast(new Goal(intent.getStringExtra(AddGoalActivity.GOAL_TITLE), intent.getStringExtra(AddGoalActivity.GOAL_DESCRIPTION), intent.getStringExtra(AddGoalActivity.GOAL_DEADLINE)));
                goalsAdapter.notifyDataSetChanged();
                db = FirebaseFirestore.getInstance();
                addDataFireStore();
            }
        }

        if (intent.hasExtra(EditGoalActivity.REMOVE_GOAL)) {
            Goal goal = (Goal) intent.getSerializableExtra(EditGoalActivity.GOAL);

            db = FirebaseFirestore.getInstance();
            deleteGoal(goal);
        }

        db = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        db.collection("users").document(fAuth.getCurrentUser().getUid()).collection("goals")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                Goal c = d.toObject(Goal.class);

                                c.setId(d.getId());

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                goals.add(c);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifuDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            goalsAdapter.notifyDataSetChanged();

                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            System.out.println("No data found in Database");
                        }
                    }
                });
        for (Goal g : goals) {
            System.out.println(g.getTitle());
        }

        //saveGoals();
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
        Goal goal = (Goal)((View)view.getParent()).getTag();
        intent.putExtra(EditGoalActivity.GOAL, goal);

        System.out.println("--------------- ID BEFORE EDIT: " + goal.getId());

        startActivity(intent);
    }

    public void addDataFireStore(){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        DocumentReference dbDocument = db.document("users/" + userId);
        if(goals.size() >0) {
            dbDocument.collection("goals").add(goals.getLast());
            goals.removeLast();/*.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(MainActivity.this, "Your goals have been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Fail to add goals \n" + e, Toast.LENGTH_SHORT).show();
                }
            })*/
        }
    }

    private void updateGoals(Goal goal, String title, String description, String deadline) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        // inside this method we are passing our updated values
        // inside our object class and later on we
        // will pass our whole object to firebase Firestore.
        Goal updatedGoal = new Goal(title, description, deadline);

        // after passing data to object class we are
        // sending it to firebase with specific document id.
        // below line is use to get the collection of our Firebase Firestore.
        System.out.println("----------- USER ID IS: " + userId);
        db.collection("users").document(userId).collection("goals").
                // below line is use toset the id of
                // document where we have to perform
                // update operation.
                        document(goal.getId()).

                // after setting our document id we are
                // passing our whole object class to it.
                        set(updatedGoal).

                // after passing our object class we are
                // calling a method for on success listener.
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // on successful completion of this process
                        // we are displaying the toast message.
                        System.out.println("Course has been updated..");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            // inside on failure method we are
            // displaying a failure message.
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Fail to update the data..");
            }
        });
    }

    private void deleteGoal(Goal goal){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("goals").
        document(goal.getId()).delete();
    }
}