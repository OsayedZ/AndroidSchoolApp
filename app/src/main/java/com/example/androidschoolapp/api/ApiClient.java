package com.example.androidschoolapp.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidschoolapp.models.Class;
import com.example.androidschoolapp.models.Subject;
import com.example.androidschoolapp.models.Task;
import com.example.androidschoolapp.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ApiClient {
    private static final String TAG = "ApiClient";
    private static final String BASE_URL = "http://10.0.2.2";
    private static ApiClient instance;
    private final RequestQueue requestQueue;
    private final Context context;
    private final SessionManager sessionManager;
    private final Gson gson;

    private ApiClient(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.sessionManager = SessionManager.getInstance(context);
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context.getApplicationContext());
        }
        return instance;
    }

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }


    private void makeApiRequest(int method, String endpoint, final Map<String, String> params, 
                               final boolean requiresAuth, final ApiResponseCallback callback) {
        // Check network connectivity
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onError(NetworkUtils.getNetworkErrorMessage());
            return;
        }
        
        String url = BASE_URL + endpoint;

        StringRequest request = new StringRequest(method, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        callback.onSuccess(jsonResponse);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing response", e);
                        callback.onError("Error parsing response: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMessage = "Network error";
                    if (error.networkResponse != null) {
                        errorMessage += " (Status: " + error.networkResponse.statusCode + ")";
                    }
                    callback.onError(errorMessage);
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                if (requiresAuth) {
                    String token = sessionManager.getToken();
                    if (token != null && !token.isEmpty()) {
                        headers.put("Authorization", "Bearer " + token);
                    }
                }
                return headers;
            }
            
            @Override
            protected Map<String, String> getParams() {
                return params != null ? params : new HashMap<>();
            }
        };

        requestQueue.add(request);
    }

    // Authentication
    public void login(String email, String password, final DataCallback<User> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Email", email);
            params.put("Password", password);
            
            makeApiRequest(Request.Method.POST, "/Authentication/Login.php", 
                          params, false, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if (response.has("Status")) {
                            String status = response.getString("Status");
                            
                            if (status.equals("Success")) {
                                // Save session
                                SessionManager sessionManager = SessionManager.getInstance(context);
                                sessionManager.saveUserLoginFromResponse(response, email);
                                
                                // Create a basic user object with info we have
                                User user = new User();
                                user.setEmail(email);
                                user.setRole(response.optString("Role", ""));
                                
                                callback.onSuccess(user);
                            } else {
                                // Error
                                String message = response.optString("Message", "Login failed");
                                callback.onError(message);
                            }
                        } else {
                            callback.onError("Invalid response format");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing login response", e);
                        callback.onError("Error parsing response: " + e.getMessage());
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating login request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }

    // Student API Methods
    public void getStudentTasks(final DataCallback<List<Task>> callback) {
        makeApiRequest(Request.Method.GET, "/Tasks/Get.php", 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.has("Status") && response.getString("Status").equals("Success")) {
                        List<Task> tasksList = new ArrayList<>();
                        
                        if (response.has("Tasks")) {
                            JSONArray tasksArray = response.getJSONArray("Tasks");
                            
                            for (int i = 0; i < tasksArray.length(); i++) {
                                JSONObject taskObject = tasksArray.getJSONObject(i);
                                Task task = new Task();
                                
                                task.setId(taskObject.optInt("ID", 0));
                                task.setType(taskObject.optString("Type", "Assignment"));
                                task.setDescription(taskObject.optString("Description", ""));
                                task.setUserId(taskObject.optInt("UserID", 0));
                                task.setMark(taskObject.optDouble("Mark", 0.0));
                                task.setSubjectId(taskObject.optInt("SubjectID", 0));
                                task.setName(taskObject.optString("Name", ""));
                                task.setAnswer(taskObject.optString("Answer", ""));
                                
                                tasksList.add(task);
                            }
                        }
                        
                        callback.onSuccess(tasksList);
                    } else {
                        String message = response.optString("Message", "Failed to fetch tasks");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing tasks response", e);
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void getStudentSchedule(final DataCallback<List<Subject>> callback) {
        makeApiRequest(Request.Method.GET, "/Schedules/StudentSchedule.php", 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.has("Status") && response.getString("Status").equals("Success")) {
                        List<Subject> scheduleList = new ArrayList<>();
                        
                        if (response.has("Schedule")) {
                            JSONArray scheduleArray = response.getJSONArray("Schedule");
                            
                            for (int i = 0; i < scheduleArray.length(); i++) {
                                JSONObject scheduleObject = scheduleArray.getJSONObject(i);
                                Subject subject = new Subject();
                                
                                subject.setName(scheduleObject.optString("Name", ""));
                                subject.setDay(scheduleObject.optInt("Day", 0));
                                subject.setStartTime(scheduleObject.optString("Start", ""));
                                subject.setEndTime(scheduleObject.optString("End", ""));
                                
                                scheduleList.add(subject);
                            }
                        }
                        
                        callback.onSuccess(scheduleList);
                    } else {
                        String message = response.optString("Message", "Failed to fetch schedule");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing schedule response", e);
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void submitTask(Task task, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Answer", task.getAnswer());
            
            makeApiRequest(Request.Method.POST, "/Tasks/SubmitTask.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        String status = response.optString("Status", "Error");
                        String message = response.optString("Message", "Operation failed");
                        
                        if (status.equals("Success")) {
                            callback.onSuccess(message);
                        } else {
                            callback.onError(message);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response", e);
                        callback.onError("Error parsing response: " + e.getMessage());
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating submit task request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }

    // Teacher API Methods
    public void getTeacherClasses(final DataCallback<List<Class>> callback) {
        makeApiRequest(Request.Method.GET, "/Classes/TeacherClasses.php", 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.has("Status") && response.getString("Status").equals("Success")) {
                        List<Class> classesList = new ArrayList<>();
                        
                        if (response.has("Classes")) {
                            JSONArray classesArray = response.getJSONArray("Classes");
                            
                            for (int i = 0; i < classesArray.length(); i++) {
                                JSONObject classObject = classesArray.getJSONObject(i);
                                Class classItem = new Class();
                                
                                classItem.setId(classObject.optInt("ID", 0));
                                classItem.setName(classObject.optString("Name", ""));
                                
                                classesList.add(classItem);
                            }
                        }
                        
                        callback.onSuccess(classesList);
                    } else {
                        String message = response.optString("Message", "Failed to fetch classes");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing classes response", e);
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void getStudentsInClass(int classId, final DataCallback<List<User>> callback) {
        makeApiRequest(Request.Method.GET, "/Classes/Students.php?classID=" + classId, 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.has("Status") && response.getString("Status").equals("Success")) {
                        List<User> studentsList = new ArrayList<>();
                        
                        if (response.has("Students")) {
                            JSONArray studentsArray = response.getJSONArray("Students");
                            
                            for (int i = 0; i < studentsArray.length(); i++) {
                                JSONObject studentObject = studentsArray.getJSONObject(i);
                                User student = new User();
                                
                                student.setId(studentObject.optInt("ID", 0));
                                student.setName(studentObject.optString("Name", ""));
                                student.setEmail(studentObject.optString("Email", ""));
                                student.setAge(studentObject.optInt("Age", 0));
                                student.setGender(studentObject.optInt("Gender", 0) == 1);
                                student.setRole("Student");
                                student.setClassId(classId);
                                
                                studentsList.add(student);
                            }
                        }
                        
                        callback.onSuccess(studentsList);
                    } else {
                        String message = response.optString("Message", "Failed to fetch students");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing students response", e);
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }


    public void getSubjectsForClass(int classId, final DataCallback<ClassSubject> callback) {
        makeApiRequest(Request.Method.GET, "/Classes/Subjects.php?classID=" + classId,
                null, true, new ApiResponseCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            if (response.has("Status") && response.getString("Status").equals("Success")) {
                                List<Subject> subjectsList = new ArrayList<>();

                                if (response.has("Subjects")) {
                                    JSONArray subjectsArray = response.getJSONArray("Subjects");

                                    for (int i = 0; i < subjectsArray.length(); i++) {
                                        JSONObject subjectObject = subjectsArray.getJSONObject(i);
                                        Subject subject = new Subject();

                                        subject.setId(subjectObject.optInt("SubjectID", 0));
                                        subject.setName(subjectObject.optString("Name", ""));
                                        subject.setTeacherId(subjectObject.optInt("TeacherID"));
                                        subject.setStartTime(subjectObject.optString("Start",""));
                                        subject.setEndTime(subjectObject.optString("End",""));
                                        subject.setDay(subjectObject.optInt("Day"));

                                        subjectsList.add(subject);
                                    }
                                }

                                callback.onSuccess(new ClassSubject(0, subjectsList, classId));
                            } else {
                                String message = response.optString("Message", "Failed to fetch subjects");
                                callback.onError(message);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing subjects response", e);
                            callback.onError("Error parsing response: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError(errorMessage);
                    }
                });
    }


    // Helper method to parse list responses
    private void parseListResponse(JSONObject response, String arrayKey, String errorMessage, 
                                  DataCallback<List<Map<String, String>>> callback) {
        try {
            if (response.has("Status") && response.getString("Status").equals("Success")) {
                List<Map<String, String>> dataList = new ArrayList<>();
                
                if (response.has(arrayKey)) {
                    JSONArray dataArray = response.getJSONArray(arrayKey);
                    
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        Map<String, String> dataMap = new HashMap<>();
                        
                        Iterator<String> keys = dataObject.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            dataMap.put(key, dataObject.getString(key));
                        }
                        
                        dataList.add(dataMap);
                    }
                }
                
                callback.onSuccess(dataList);
            } else {
                String message = response.optString("Message", errorMessage);
                callback.onError(message);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing response", e);
            callback.onError("Error parsing response: " + e.getMessage());
        }
    }


    public void addTask(Task task, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Name", task.getName());
            params.put("Type", task.getType());
            params.put("Description", task.getDescription());
            params.put("SubjectID", String.valueOf(task.getSubjectId()));
            
            makeApiRequest(Request.Method.POST, "/Tasks/Add.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        String status = response.optString("Status", "Error");
                        String message = response.optString("Message", "Operation failed");

                        if (status.equals("Success")) {
                            callback.onSuccess(message);
                        } else {
                            callback.onError(message);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing simple response", e);
                        callback.onError("Error parsing response: " + e.getMessage());
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating add task request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }

    private interface ApiResponseCallback {
        void onSuccess(JSONObject response);
        void onError(String errorMessage);
    }
    
    // Method to edit a task
    public void editTask(Task task, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Name", task.getName());
            params.put("Type", task.getType());
            params.put("Description", task.getDescription());
            params.put("SubjectID", String.valueOf(task.getSubjectId()));
            
            makeApiRequest(Request.Method.POST, "/Tasks/Edit.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating edit task request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to delete a task
    public void deleteTask(Task task, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Name", task.getName());
            params.put("SubjectID", String.valueOf(task.getSubjectId()));
            
            makeApiRequest(Request.Method.POST, "/Tasks/Delete.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating delete task request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to grade a task
    public void gradeTask(Task task, int studentId, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Mark", String.valueOf(task.getMark()));
            params.put("StudentID", String.valueOf(studentId));
            params.put("Name", task.getName());
            params.put("SubjectID", String.valueOf(task.getSubjectId()));
            
            makeApiRequest(Request.Method.POST, "/Tasks/GradeTask.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating grade task request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }

    // Method to get all teachers
    public void getTeachers(final DataCallback<List<User>> callback) {
        makeApiRequest(Request.Method.GET, "/Teachers/Get.php",
                null, true, new ApiResponseCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            if (response.has("Status") && response.getString("Status").equals("Success")) {
                                List<User> teachersList = new ArrayList<>();

                                if (response.has("Teachers")) {
                                    JSONArray teachersArray = response.getJSONArray("Teachers");

                                    for (int i = 0; i < teachersArray.length(); i++) {
                                        JSONObject teacherObject = teachersArray.getJSONObject(i);
                                        User teacherItem = new User();

                                        teacherItem.setId(teacherObject.optInt("ID"));
                                        teacherItem.setName(teacherObject.optString("Name", ""));
                                        teacherItem.setEmail(teacherObject.optString("Email", ""));
                                        teacherItem.setAge(teacherObject.optInt("Age", 0));

                                        // Handle gender (from string or int)
                                        if (teacherObject.has("Gender")) {
                                            try {
                                                // Try to parse as integer
                                                int genderInt = teacherObject.getInt("Gender");
                                                teacherItem.setGender(genderInt == 1);
                                            } catch (JSONException e) {
                                                // Try to parse as string
                                                String genderStr = teacherObject.getString("Gender");
                                                teacherItem.setGender(genderStr.equalsIgnoreCase("Male") || genderStr.equals("1"));
                                            }
                                        }

                                        teacherItem.setClassId(teacherObject.optInt("ClassID", 0));
                                        teacherItem.setRole("Teacher");

                                        teachersList.add(teacherItem);
                                    }
                                }

                                callback.onSuccess(teachersList);
                            } else {
                                String message = response.optString("Message", "Failed to fetch classes");
                                callback.onError(message);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing classes response", e);
                            callback.onError("Error parsing response: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError(errorMessage);
                    }
                });
    }
    
    // Method to get teacher schedule
    public void getTeacherSchedule(final DataCallback<List<Subject>> callback) {
        makeApiRequest(Request.Method.GET, "/Schedules/TeacherSchedule.php", 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.has("Status") && response.getString("Status").equals("Success")) {
                        List<Subject> scheduleList = new ArrayList<>();
                        
                        if (response.has("Schedule")) {
                            JSONArray scheduleArray = response.getJSONArray("Schedule");
                            
                            for (int i = 0; i < scheduleArray.length(); i++) {
                                JSONObject scheduleObject = scheduleArray.getJSONObject(i);
                                Subject subject = new Subject();
                                
                                subject.setName(scheduleObject.optString("Name", ""));
                                subject.setDay(scheduleObject.optInt("Day", 0));
                                subject.setStartTime(scheduleObject.optString("Start", ""));
                                subject.setEndTime(scheduleObject.optString("End", ""));
                                
                                scheduleList.add(subject);
                            }
                        }
                        
                        callback.onSuccess(scheduleList);
                    } else {
                        String message = response.optString("Message", "Failed to fetch schedule");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing schedule response", e);
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
    
    // Helper method to handle simple response
    private void handleSimpleResponse(JSONObject response, DataCallback<String> callback) {
        try {
            String status = response.optString("Status", "Error");
            String message = response.optString("Message", "Operation failed");

            if (status.equals("Success")) {
                callback.onSuccess(message);
            } else {
                callback.onError(message);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing simple response", e);
            callback.onError("Error parsing response: " + e.getMessage());
        }
    }
    
    // Registrar API methods
    
    // Method to get all classes
    public void getClasses(final DataCallback<List<Class>> callback) {
        makeApiRequest(Request.Method.GET, "/Classes/Get.php", 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.has("Status") && response.getString("Status").equals("Success")) {
                        List<Class> classesList = new ArrayList<>();
                        
                        if (response.has("Classes")) {
                            JSONArray classesArray = response.getJSONArray("Classes");
                            
                            for (int i = 0; i < classesArray.length(); i++) {
                                JSONObject classObject = classesArray.getJSONObject(i);
                                Class classItem = new Class();
                                
                                classItem.setId(classObject.optInt("ID", 0));
                                classItem.setName(classObject.optString("Name", ""));
                                
                                classesList.add(classItem);
                            }
                        }
                        
                        callback.onSuccess(classesList);
                    } else {
                        String message = response.optString("Message", "Failed to fetch classes");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing classes response", e);
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
    
    // Method to add a class
    public void addClass(Class classItem, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Name", classItem.getName());
            
            makeApiRequest(Request.Method.POST, "/Classes/Add.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating add class request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to edit a class
    public void editClass(Class classItem, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Name", classItem.getName());
            params.put("ID", String.valueOf(classItem.getId()));
            
            makeApiRequest(Request.Method.POST, "/Classes/Edit.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating edit class request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to delete a class
    public void deleteClass(int classId, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("ID", String.valueOf(classId));
            
            makeApiRequest(Request.Method.POST, "/Classes/Delete.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating delete class request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to get all students
    public void getStudents(final DataCallback<List<User>> callback) {
        makeApiRequest(Request.Method.GET, "/Students/Get.php", 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.has("Status") && response.getString("Status").equals("Success")) {
                        List<User> studentsList = new ArrayList<>();
                        
                        if (response.has("Students")) {
                            JSONArray studentsArray = response.getJSONArray("Students");
                            
                            for (int i = 0; i < studentsArray.length(); i++) {
                                JSONObject studentObject = studentsArray.getJSONObject(i);
                                User student = new User();

                                student.setId(studentObject.optInt("ID"));
                                student.setName(studentObject.optString("Name", ""));
                                student.setEmail(studentObject.optString("Email", ""));
                                student.setAge(studentObject.optInt("Age", 0));
                                
                                // Handle gender (from string or int)
                                if (studentObject.has("Gender")) {
                                    try {
                                        // Try to parse as integer
                                        int genderInt = studentObject.getInt("Gender");
                                        student.setGender(genderInt == 1);
                                    } catch (JSONException e) {
                                        // Try to parse as string
                                        String genderStr = studentObject.getString("Gender");
                                        student.setGender(genderStr.equalsIgnoreCase("Male") || genderStr.equals("1"));
                                    }
                                }
                                
                                student.setClassId(studentObject.optInt("ClassID", 0));
                                student.setRole("Student");
                                
                                studentsList.add(student);
                            }
                        }
                        
                        callback.onSuccess(studentsList);
                    } else {
                        String message = response.optString("Message", "Failed to fetch students");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing students response", e);
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
    
    // Method to add a student or teacher
    public void addStudentOrTeacher(User user, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Name", user.getName());
            params.put("Email", user.getEmail());
            params.put("Age", String.valueOf(user.getAge()));
            params.put("Gender", user.getGenderString());
            params.put("Password", user.getPassword());
            params.put("ClassID", String.valueOf(user.getClassId()));
            params.put("Role", user.getRole());
            
            makeApiRequest(Request.Method.POST, "/Registrars/AddST.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating add student/teacher request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to edit a student or teacher
    public void editStudentOrTeacher(User user, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Name", user.getName());
            params.put("Email", user.getEmail());
            params.put("Age", String.valueOf(user.getAge()));
            params.put("Gender", user.getGenderString());
            params.put("Password", user.getPassword());
            params.put("ID", String.valueOf(user.getId()));
            
            makeApiRequest(Request.Method.POST, "/Registrars/EditST.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating edit student/teacher request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to delete a student or teacher
    public void deleteStudentOrTeacher(int userId, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("ID", String.valueOf(userId));
            
            makeApiRequest(Request.Method.POST, "/Registrars/DeleteST.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating delete student/teacher request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to set student's class
    public void setStudentClass(int studentId, int classId, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("StudentID", String.valueOf(studentId));
            params.put("ClassID", String.valueOf(classId));
            
            makeApiRequest(Request.Method.POST, "/Students/SetClass.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating set student class request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to get all subjects
    public void getSubjects(final DataCallback<List<Subject>> callback) {
        makeApiRequest(Request.Method.GET, "/Subjects/Get.php", 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.has("Status") && response.getString("Status").equals("Success")) {
                        List<Subject> subjectsList = new ArrayList<>();
                        
                        if (response.has("Subjects")) {
                            JSONArray subjectsArray = response.getJSONArray("Subjects");
                            
                            for (int i = 0; i < subjectsArray.length(); i++) {
                                JSONObject subjectObject = subjectsArray.getJSONObject(i);
                                Subject subject = new Subject();
                                
                                subject.setId(subjectObject.optInt("ID", 0));
                                subject.setName(subjectObject.optString("Name", ""));
                                subject.setTeacherName(subjectObject.optString("TeacherName", ""));
                                subject.setTeacherId(subjectObject.optInt("TeacherID", 0));
                                subject.setStartTime(subjectObject.optString("Start", ""));
                                subject.setEndTime(subjectObject.optString("End", ""));
                                subject.setDay(subjectObject.optInt("Day", 0));
                                
                                subjectsList.add(subject);
                            }
                        }
                        
                        callback.onSuccess(subjectsList);
                    } else {
                        String message = response.optString("Message", "Failed to fetch subjects");
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing subjects response", e);
                    callback.onError("Error parsing response: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
    
    // Method to add a subject
    public void addSubject(Subject subject, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Name", subject.getName());
            params.put("TeacherID", String.valueOf(subject.getTeacherId()));
            params.put("Start", subject.getStartTime());
            params.put("End", subject.getEndTime());
            params.put("Day", String.valueOf(subject.getDay()));
            
            makeApiRequest(Request.Method.POST, "/Subjects/Add.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating add subject request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to edit a subject
    public void editSubject(Subject subject, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("ID", String.valueOf(subject.getId()));
            params.put("Name", subject.getName());
            params.put("TeacherID", String.valueOf(subject.getTeacherId()));
            params.put("Start", subject.getStartTime());
            params.put("End", subject.getEndTime());
            params.put("Day", String.valueOf(subject.getDay()));
            
            makeApiRequest(Request.Method.POST, "/Subjects/Edit.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating edit subject request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to delete a subject
    public void deleteSubject(int subjectId, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("ID", String.valueOf(subjectId));
            
            makeApiRequest(Request.Method.POST, "/Subjects/Delete.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating delete subject request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to assign subject to class
    public void assignSubjectToClass(int subjectId, int classId, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("SubjectID", String.valueOf(subjectId));
            params.put("ClassID", String.valueOf(classId));
            
            makeApiRequest(Request.Method.POST, "/Classes/AssignSubject.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating assign subject request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to unassign subject from class
    public void unassignSubjectFromClass(int subjectId, int classId, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("SubjectID", String.valueOf(subjectId));
            params.put("ClassID", String.valueOf(classId));

            makeApiRequest(Request.Method.POST, "/Classes/UnassignSubject.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating unassign subject request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
    
    // Method to assign teacher to subject
    public void assignTeacherToSubject(int teacherId, int subjectId, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("TeacherID", String.valueOf(teacherId));
            params.put("SubjectID", String.valueOf(subjectId));
            
            makeApiRequest(Request.Method.POST, "/Subjects/AssignTeacher.php", 
                          params, true, new ApiResponseCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    handleSimpleResponse(response, callback);
                }

                @Override
                public void onError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating assign teacher request", e);
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
}
