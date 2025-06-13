package com.example.androidschoolapp.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.api.SessionManager;

/**
 * Base activity class that provides common functionality for all activities
 * in the application.
 */
public abstract class BaseActivity extends AppCompatActivity {
    
    protected ApiClient apiClient;
    protected LoadingDialog loadingDialog;
    protected String screenTitle;
    protected SessionManager sessionManager;

    public BaseActivity() {
        this.screenTitle = null;
    }

    public BaseActivity(String title) {
        this.screenTitle = title;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        // Initialize common components
        apiClient = ApiClient.getInstance(this);
        loadingDialog = new LoadingDialog(this);
        sessionManager = SessionManager.getInstance(this);
    }
    
    /**
     * Sets up the content view and edge-to-edge display for the activity
     * @param layoutResId Layout resource ID
     * @param rootViewId Root view ID for edge-to-edge insets
     */
    protected void setupContentView(@LayoutRes int layoutResId, int rootViewId) {
        setContentView(layoutResId);
        setupEdgeToEdge(rootViewId);
        
        // Setup toolbar if it exists
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                // For dashboard activities, don't show back button
                if (!isDashboardActivity()) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
                if (screenTitle != null) {
                    getSupportActionBar().setTitle(screenTitle);
                }
            }
        }
    }
    
    /**
     * Set up edge-to-edge content with proper insets handling
     * @param rootViewId ID of the root view in the layout
     */
    protected void setupEdgeToEdge(int rootViewId) {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(rootViewId), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    /**
     * Show a short toast message
     * @param message Message to display
     */
    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Show a long toast message
     * @param message Message to display
     */
    protected void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Open the loading dialog
     */
    protected void startLoading()
    {
        loadingDialog.show();
    }

    /**
     * Dismiss the loading dialog
     */
    protected void endLoading() {
        loadingDialog.dismiss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu with logout option for dashboard activities
        if (isDashboardActivity()) {
            getMenuInflater().inflate(R.menu.dashboard_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Check if current activity is a dashboard activity
     */
    protected boolean isDashboardActivity() {
        String className = this.getClass().getSimpleName();
        return className.contains("Dashboard");
    }

    /**
     * Show logout confirmation dialog
     */
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Perform logout operation
     */
    protected void performLogout() {
        sessionManager.logoutUser();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        showToast("Logged out successfully");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

} 