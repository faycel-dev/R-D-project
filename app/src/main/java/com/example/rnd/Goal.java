package com.example.rnd;

public class Goal {
    private String title, description, deadline;

    public Goal(String title, String description, String deadline) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setTitle(String title) { this.title = title; }

    public void setDescription(String description) { this.description = description; }

    public void setDeadline(String deadline) { this.deadline = deadline; }
}
