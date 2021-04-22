package com.example.rnd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddGoalActivity extends AppCompatActivity {

    public static final String GOAL_TITLE = "com.example.rnd.GOAL_TITLE";
    public static final String GOAL_DESCRIPTION = "com.example.rnd.GOAL_DESCRIPTION";
    public static final String GOAL_DEADLINE = "com.example.rnd.GOAL_DEADLINE";

    EditText title, description, deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        title = findViewById(R.id.etGoalTitle);
        description = findViewById(R.id.etGoalDescription);
        deadline = findViewById(R.id.etDeadline);
    }

    public void onDone(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(GOAL_TITLE, title.getText().toString());
        intent.putExtra(GOAL_DESCRIPTION, description.getText().toString());
        intent.putExtra(GOAL_DEADLINE, deadline.getText().toString());
        startActivity(intent);
    }
}
