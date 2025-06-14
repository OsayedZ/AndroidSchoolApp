package com.example.androidschoolapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task {

    public enum TaskType {
        ASSIGNMENT("Assignment"),
        EXAM("Exam");
        
        private final String value;
        
        TaskType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static TaskType fromString(String text) {
            for (TaskType type : TaskType.values()) {
                if (type.value.equalsIgnoreCase(text)) {
                    return type;
                }
            }
            return ASSIGNMENT; // Default value
        }
    }
    
    @SerializedName("ID")
    @Expose
    private int id;
    
    @SerializedName("Type")
    @Expose
    private String type;
    
    @SerializedName("Description")
    @Expose
    private String description;
    
    @SerializedName("UserID")
    @Expose
    private int userId;
    
    @SerializedName("Mark")
    @Expose
    private double mark;
    
    @SerializedName("SubjectID")
    @Expose
    private int subjectId;
    
    @SerializedName("Name")
    @Expose
    private String name;
    
    @SerializedName("Answer")
    @Expose
    private String answer;

    @SerializedName("Due")
    @Expose
    private String dueDate;

    @SerializedName("StudentName")
    @Expose
    private String studentName;

    @SerializedName("StudentEmail")
    @Expose
    private String studentEmail;
    
    // Constructors
    public Task() {
    }
    
    public Task(int id, String type, String description, int userId, double mark, int subjectId, String name, String answer) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.mark = mark;
        this.subjectId = subjectId;
        this.name = name;
        this.answer = answer;
    }

    public Task(String name, String type, String description, String dueDate) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.dueDate = dueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    
    public TaskType getTaskType() {
        return TaskType.fromString(type);
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setTaskType(TaskType taskType) {
        this.type = taskType.getValue();
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public double getMark() {
        return mark;
    }
    
    public void setMark(double mark) {
        this.mark = mark;
    }
    
    public int getSubjectId() {
        return subjectId;
    }
    
    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
}