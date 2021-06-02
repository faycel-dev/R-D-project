package com.example.rdapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.model.mutation.ArrayTransformOperation;

public class EditGoalActivity extends AppCompatActivity {
    public static final String GOAL_TITLE = "com.example.rnd.GOAL_TITLE";
    public static final String GOAL_DESCRIPTION = "com.example.rnd.GOAL_DESCRIPTION";
    public static final String GOAL_DEADLINE = "com.example.rnd.GOAL_DEADLINE";
    public static final String GOAL = "com.example.rnd.GOAL";
    public static final String EDIT_GOAL = "com.example.rnd.EDIT";
    public static final String GOAL_INDEX = "com.example.rnd.GOAL_INDEX";
    public static final String REMOVE_GOAL = "com.example.rnd.REMOVEGOAL";

    int goalIndex;
    EditText title, description, deadline;
    Goal goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);

        title = findViewById(R.id.etGoalTitle);
        description = findViewById(R.id.etGoalDescription);
        deadline = findViewById(R.id.etDeadline);

        Intent intent = getIntent();
        if (intent.hasExtra(GOAL)) {
            goal = (Goal)intent.getSerializableExtra(GOAL);
        }

        title.setText(goal.getTitle());
        description.setText(goal.getDescription());
        deadline.setText(goal.getDeadline());

        title = findViewById(R.id.etGoalTitle);
        description = findViewById(R.id.etGoalDescription);
        deadline = findViewById(R.id.etDeadline);
    }

    public void onDone(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EDIT_GOAL, "");
        intent.putExtra(GOAL, goal);
        intent.putExtra(GOAL_TITLE, title.getText().toString());
        intent.putExtra(GOAL_DESCRIPTION, description.getText().toString());
        intent.putExtra(GOAL_DEADLINE, deadline.getText().toString());
        startActivity(intent);
    }

    public void onRemove(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(REMOVE_GOAL, "");
        intent.putExtra(GOAL, goal);
        startActivity(intent);
    }
}
