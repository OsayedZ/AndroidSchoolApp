package com.example.androidschoolapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassSubject {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("SubjectID")
    @Expose
    private int subjectId;

    @SerializedName("ClassID")
    @Expose
    private int classId;

    // Constructors
    public ClassSubject() {
    }

    public ClassSubject(int id, int subjectId, int classId) {
        this.id = id;
        this.subjectId = subjectId;
        this.classId = classId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
} 