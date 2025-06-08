package com.example.androidschoolapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    @SerializedName("ID")
    @Expose
    private int id;
    
    @SerializedName("Name")
    @Expose
    private String name;
    
    @SerializedName("Email")
    @Expose
    private String email;
    
    @SerializedName("Age")
    @Expose
    private int age;
    
    @SerializedName("Gender")
    @Expose
    private boolean gender; // true for male, false for female
    
    @SerializedName("Role")
    @Expose
    private String role;
    
    @SerializedName("Password")
    @Expose
    private String password;
    
    @SerializedName("Date")
    @Expose
    private Date registrationDate;
    
    @SerializedName("ClassID")
    @Expose
    private int classId;
    
    // Constructors
    public User() {
    }
    
    public User(int id, String name, String email, int age, boolean gender, String role, String password, Date registrationDate, int classId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.password = password;
        this.registrationDate = registrationDate;
        this.classId = classId;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public boolean isGender() {
        return gender;
    }
    
    public String getGenderString() {
        return gender ? "Male" : "Female";
    }
    
    public void setGender(boolean gender) {
        this.gender = gender;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Date getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public int getClassId() {
        return classId;
    }
    
    public void setClassId(int classId) {
        this.classId = classId;
    }
    
    @Override
    public String toString() {
        return name;
    }
} 