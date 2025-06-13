package com.example.androidschoolapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassSubject {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("Subjects")
    @Expose
    private List<Subject> subjects;

    @SerializedName("ClassID")
    @Expose
    private int classId;

    // Constructors
    public ClassSubject() {
    }

    public ClassSubject(int id, List<Subject> subjects, int classId) {
        this.id = id;
        this.subjects = subjects;
        this.classId = classId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
} 