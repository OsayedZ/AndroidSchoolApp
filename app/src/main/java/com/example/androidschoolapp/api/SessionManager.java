package com.example.androidschoolapp.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    private static final String TAG = "SessionManager";
    private static final String PREF_NAME = "SchoolAppSession";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ROLE = "role";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private static SessionManager instance;

    private SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveUserLoginSession(String token, String role, String email) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_EMAIL, email);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
        
        Log.d(TAG, "User session saved. Role: " + role);
    }

    public void saveUserLoginFromResponse(JSONObject response, String email) {
        try {
            if (response.has("Status") && response.getString("Status").equals("Success")) {
                String token = response.getString("Token");
                String role = response.getString("Role");
                saveUserLoginSession(token, role, email);
                return;
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing login response", e);
        }
        
        Log.e(TAG, "Failed to save user session from response");
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
        
        Log.d(TAG, "User logged out");
    }

    public boolean isStudent() {
        String role = getRole();
        return role != null && role.equalsIgnoreCase("Student");
    }

    public boolean isTeacher() {
        String role = getRole();
        return role != null && role.equalsIgnoreCase("Teacher");
    }

    public boolean isRegistrar() {
        String role = getRole();
        return role != null && role.equalsIgnoreCase("Registrar");
    }
} 