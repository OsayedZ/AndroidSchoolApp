package com.example.androidschoolapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Class {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("Name")
    @Expose
    private String name;

    // Constructors
    public Class() {
    }

    public Class(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
} 