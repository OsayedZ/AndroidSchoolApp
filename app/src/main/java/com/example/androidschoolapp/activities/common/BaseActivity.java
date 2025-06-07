package com.example.androidschoolapp.activities.common;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.api.ApiClient;

/**
 * Base activity class that provides common functionality for all activities
 * in the application.
 */
public abstract class BaseActivity extends AppCompatActivity {
    
    protected ApiClient apiClient;
    protected LoadingDialog loadingDialog;
    protected String screenTitle;

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
    }
    
    /**
     * Sets up the content view and edge-to-edge display for the activity
     * @param layoutResId Layout resource ID
     * @param rootViewId Root view ID for edge-to-edge insets
     */
    protected void setupContentView(@LayoutRes int layoutResId, int rootViewId) {
        setContentView(layoutResId);
        setupEdgeToEdge(rootViewId);
        
        // Setup toolbar with title if provided
        if (screenTitle != null) {
            setSupportActionBar(findViewById(R.id.toolbar));
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(screenTitle);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

} 