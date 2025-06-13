package com.example.androidschoolapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subject {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("TeacherName")
    @Expose
    private String teacherName;

    @SerializedName("TeacherID")
    @Expose
    private int teacherId;

    @SerializedName("Start")
    @Expose
    private String startTime;

    @SerializedName("End")
    @Expose
    private String endTime;

    @SerializedName("Day")
    @Expose
    private int day;

    // Constructors
    public Subject() {
    }

    public Subject(int id, String name, String teacherName, int teacherId, String startTime, String endTime, int day) {
        this.id = id;
        this.name = name;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    public Subject(int id, String name, int day, String startTime, String endTime) {
        this.id = id;
        this.name = name;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return name;
    }
} 