package com.example.rdapp;

import java.io.Serializable;

public class Goal implements Serializable {
    private String title, description, deadline, id;

    public Goal(String title, String description, String deadline) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }
    public Goal(){

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

    public String getId(){ return id; }

    public void setTitle(String title) { this.title = title; }

    public void setDescription(String description) { this.description = description; }

    public void setDeadline(String deadline) { this.deadline = deadline; }

    public void setId(String id) {this.id = id; }
}
