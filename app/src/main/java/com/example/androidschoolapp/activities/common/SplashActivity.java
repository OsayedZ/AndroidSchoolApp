package com.example.androidschoolapp.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.registrar.RegistrarDashboardActivity;
import com.example.androidschoolapp.activities.student.StudentDashboardActivity;
import com.example.androidschoolapp.activities.teacher.TeacherDashboardActivity;
import com.example.androidschoolapp.api.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = SessionManager.getInstance(this);

        // Delay and then check session
        new Handler(Looper.getMainLooper()).postDelayed(this::checkUserSession, SPLASH_DELAY);
    }

    private void checkUserSession() {
//        if (sessionManager.isLoggedIn()) {
//            // User is logged in, navigate to appropriate dashboard
//            navigateToDashboard();
//        } else {
            // User is not logged in, navigate to login
            navigateToLogin();
//        }
    }

    private void navigateToDashboard() {
        String role = sessionManager.getRole();
        Class<?> destinationClass = LoginActivity.class; // Default fallback

        if (role != null) {
            switch (role.toLowerCase()) {
                case "student":
                    destinationClass = StudentDashboardActivity.class;
                    break;
                case "teacher":
                    destinationClass = TeacherDashboardActivity.class;
                    break;
                case "registrar":
                    destinationClass = RegistrarDashboardActivity.class;
                    break;
                default:
                    destinationClass = LoginActivity.class;
                    break;
            }
        }

        Intent intent = new Intent(SplashActivity.this, destinationClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 