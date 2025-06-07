package com.example.androidschoolapp.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

    private ApiClient(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.sessionManager = SessionManager.getInstance(context);
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
    public void login(String email, String password, final DataCallback<String> callback) {
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
                                
                                String role = response.getString("Role");
                                callback.onSuccess(role);
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
    public void getStudentTasks(final DataCallback<List<Map<String, String>>> callback) {
        makeApiRequest(Request.Method.GET, "/Tasks/Get.php", 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.has("Status") && response.getString("Status").equals("Success")) {
                        List<Map<String, String>> tasksList = new ArrayList<>();
                        
                        if (response.has("Tasks")) {
                            JSONArray tasksArray = response.getJSONArray("Tasks");
                            
                            for (int i = 0; i < tasksArray.length(); i++) {
                                JSONObject taskObject = tasksArray.getJSONObject(i);
                                Map<String, String> taskMap = new HashMap<>();
                                
                                Iterator<String> keys = taskObject.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    taskMap.put(key, taskObject.getString(key));
                                }
                                
                                tasksList.add(taskMap);
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

    public void getStudentSchedule(final DataCallback<List<Map<String, String>>> callback) {
        makeApiRequest(Request.Method.GET, "/Schedules/StudentSchedule.php", 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.has("Status") && response.getString("Status").equals("Success")) {
                        List<Map<String, String>> scheduleList = new ArrayList<>();
                        
                        if (response.has("Schedule")) {
                            JSONArray scheduleArray = response.getJSONArray("Schedule");
                            
                            for (int i = 0; i < scheduleArray.length(); i++) {
                                JSONObject scheduleObject = scheduleArray.getJSONObject(i);
                                Map<String, String> scheduleMap = new HashMap<>();
                                
                                Iterator<String> keys = scheduleObject.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    scheduleMap.put(key, scheduleObject.getString(key));
                                }
                                
                                scheduleList.add(scheduleMap);
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

    public void submitTask(String answer, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Answer", answer);
            
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
    public void getTeacherClasses(final DataCallback<List<Map<String, String>>> callback) {
        makeApiRequest(Request.Method.GET, "/Classes/TeacherClasses.php", 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                parseListResponse(response, "Classes", "Failed to fetch classes", callback);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void getStudentsInClass(String classId, final DataCallback<List<Map<String, String>>> callback) {
        makeApiRequest(Request.Method.GET, "/Classes/Students.php?classID=" + classId, 
                      null, true, new ApiResponseCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                parseListResponse(response, "Students", "Failed to fetch students", callback);
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


    public void addTask(String name, String type, String description,
                      int subjectId, final DataCallback<String> callback) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("Name", name);
            params.put("Type", type);
            params.put("Description", description);
            params.put("SubjectID", String.valueOf(subjectId));
            
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
}
