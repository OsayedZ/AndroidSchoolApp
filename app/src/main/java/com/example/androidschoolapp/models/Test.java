package com.example.androidschoolapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Test {
    @SerializedName("ID")
    @Expose
    private int id;
    
    @SerializedName("Start")
    @Expose
    private String startTime;
    
    @SerializedName("End")
    @Expose
    private String endTime;
    
    // Constructors
    public Test() {
    }
    
    public Test(int id, String startTime, String endTime) {
        this.id = id;
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
} 