package com.example.rdapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditGoalActivity extends AppCompatActivity {
    public static final String GOAL_TITLE = "com.example.rnd.GOAL_TITLE";
    public static final String GOAL_DESCRIPTION = "com.example.rnd.GOAL_DESCRIPTION";
    public static final String GOAL_DEADLINE = "com.example.rnd.GOAL_DEADLINE";
    public static final String EDIT_GOAL = "com.example.rnd.EDIT";
    public static final String GOAL_INDEX = "com.example.rnd.GOAL_INDEX";

    int goalIndex;
    EditText title, description, deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);

        title = findViewById(R.id.etGoalTitle);
        description = findViewById(R.id.etGoalDescription);
        deadline = findViewById(R.id.etDeadline);

        Intent intent = getIntent();

        if (intent.hasExtra(GOAL_DEADLINE) && intent.hasExtra(GOAL_DESCRIPTION) && intent.hasExtra(GOAL_TITLE)) {
            title.setText(intent.getStringExtra(GOAL_TITLE));
            description.setText(intent.getStringExtra(GOAL_DESCRIPTION));
            deadline.setText(intent.getStringExtra(GOAL_DEADLINE));
        }

        if (intent.hasExtra(GOAL_INDEX)) {
            goalIndex = intent.getIntExtra(GOAL_INDEX, 0);
        }

        title = findViewById(R.id.etGoalTitle);
        description = findViewById(R.id.etGoalDescription);
        deadline = findViewById(R.id.etDeadline);
    }

    public void onDone(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EDIT_GOAL, "");
        intent.putExtra(GOAL_INDEX, goalIndex);
        intent.putExtra(GOAL_TITLE, title.getText().toString());
        intent.putExtra(GOAL_DESCRIPTION, description.getText().toString());
        intent.putExtra(GOAL_DEADLINE, deadline.getText().toString());
        startActivity(intent);
    }
}
