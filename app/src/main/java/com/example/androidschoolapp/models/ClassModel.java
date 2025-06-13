package com.example.androidschoolapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassModel {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("NumberOfStudent")
    @Expose
    private String numberOfStudent;

    @SerializedName("NumberOfSubjects")
    @Expose
    private String numberOfSubjects;

    // Constructors
    public ClassModel() {
    }

    public ClassModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ClassModel(int id, String name, String numberOfStudent, String numberOfSubjects) {
        this.id = id;
        this.name = name;
        this.numberOfStudent = numberOfStudent;
        this.numberOfSubjects = numberOfSubjects;
    }

    public String getNumberOfStudent() {
        return numberOfStudent;
    }

    public void setNumberOfStudent(String numberOfStudent) {
        this.numberOfStudent = numberOfStudent;
    }

    public String getNumberOfSubjects() {
        return numberOfSubjects;
    }

    public void setNumberOfSubjects(String numberOfSubjects) {
        this.numberOfSubjects = numberOfSubjects;
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