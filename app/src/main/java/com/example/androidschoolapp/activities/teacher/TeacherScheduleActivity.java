package com.example.androidschoolapp.activities.teacher;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.adapters.ScheduleAdapter;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Subject;
import com.example.androidschoolapp.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TeacherScheduleActivity extends BaseActivity {
    
    private RecyclerView scheduleRecyclerView;
    private ScheduleAdapter scheduleAdapter;
    private ProgressBar progressLoading;
    private LinearLayout emptyView;
    private TextView scheduleHeader;
    private TextView weekLabelTextView;
    private LinearLayout dayItemsContainer;
    
    private ApiClient apiClient;
    private List<Subject> allScheduleData;
    private List<DateUtils.DayItem> dayItems;
    private int selectedDayIndex = -1;

    public TeacherScheduleActivity() {
        super("Schedule");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_teacher_schedule, R.id.teacher_schedule_root);
        
        initializeViews();
        setupDaySelector();
        setupRecyclerView();
        loadScheduleData();
    }
    
    private void initializeViews() {
        scheduleRecyclerView = findViewById(R.id.schedule_recycler_view);
        progressLoading = findViewById(R.id.progress_loading);
        emptyView = findViewById(R.id.empty_view);
        scheduleHeader = findViewById(R.id.schedule_header);
        weekLabelTextView = findViewById(R.id.tv_week_label);
        dayItemsContainer = findViewById(R.id.layout_day_items);
        
        apiClient = ApiClient.getInstance(this);
        allScheduleData = new ArrayList<>();
    }
    
    private void setupDaySelector() {
        dayItems = DateUtils.getWeekDays();
        dayItemsContainer.removeAllViews();
        
        for (int i = 0; i < dayItems.size(); i++) {
            DateUtils.DayItem dayItem = dayItems.get(i);
            View dayView = createDayItemView(dayItem, i);
            dayItemsContainer.addView(dayView);
            
            if (dayItem.isToday()) {
                selectedDayIndex = i;
            }
        }

        updateDaySelector(); // Update UI to reflect the selection

    }
    
    private View createDayItemView(DateUtils.DayItem dayItem, int index) {
        View dayView = LayoutInflater.from(this).inflate(R.layout.item_date_selector, null);
        
        TextView dayNameTextView = dayView.findViewById(R.id.tv_day_name);
        dayNameTextView.setText(dayItem.getDayName());
        
        // Set selection state
        updateDayItemSelection(dayView, dayItem.isSelected());
        
        // Set click listener
        dayView.setOnClickListener(v -> {
            // Update selection
            for (DateUtils.DayItem item : dayItems) {
                item.setSelected(false);
            }
            dayItem.setSelected(true);
            selectedDayIndex = index;
            
            // Update UI
            updateDaySelector();
            filterScheduleByDay(dayItem.getDayNumber());
            updateScheduleHeader(dayItem);
        });
        
        return dayView;
    }
    
    private void updateDayItemSelection(View dayView, boolean isSelected) {
        TextView dayNameTextView = dayView.findViewById(R.id.tv_day_name);
        
        if (isSelected) {
            dayView.setBackground(ContextCompat.getDrawable(this, R.drawable.schedule_date_selected));
            dayNameTextView.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            dayView.setBackground(ContextCompat.getDrawable(this, R.drawable.schedule_room_background));
            dayNameTextView.setTextColor(ContextCompat.getColor(this, R.color.schedule_text_primary));
        }
    }
    
    private void updateDaySelector() {
        for (int i = 0; i < dayItemsContainer.getChildCount(); i++) {
            View dayView = dayItemsContainer.getChildAt(i);
            DateUtils.DayItem dayItem = dayItems.get(i);
            updateDayItemSelection(dayView, dayItem.isSelected());
        }
    }
    
    private void updateScheduleHeader(DateUtils.DayItem dayItem) {
        if (dayItem.isToday()) {
            scheduleHeader.setText("Today's Classes");
        } else {
            String fullDayName = DateUtils.getDayFullName(dayItem.getDayNumber());
            scheduleHeader.setText(fullDayName + "'s Classes");
        }
    }
    
    private void setupRecyclerView() {
        scheduleAdapter = new ScheduleAdapter(this, new ArrayList<>());
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleRecyclerView.setAdapter(scheduleAdapter);
    }
    
    private void loadScheduleData() {
        showLoading(true);
        
        apiClient.getTeacherSchedule(new ApiClient.DataCallback<List<Subject>>() {
            @Override
            public void onSuccess(List<Subject> result) {
                runOnUiThread(() -> {
                    showLoading(false);
                    allScheduleData = result;
                    
                    if (result.isEmpty()) {
                        showEmptyState(true);
                    } else {
                        showEmptyState(false);
                        // Show today's schedule by default
                        int todayDayNumber = DateUtils.getTodayDayNumber();
                        filterScheduleByDay(todayDayNumber);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showEmptyState(true);
                    Toast.makeText(TeacherScheduleActivity.this, 
                        "Failed to load schedule: " + errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    private void filterScheduleByDay(int dayOfWeek) {
        List<Subject> filteredSchedule = new ArrayList<>();
        
        for (Subject subject : allScheduleData) {
            if (subject.getDay() == dayOfWeek) {
                filteredSchedule.add(subject);
            }
        }
        
        // Sort by start time
        filteredSchedule.sort((s1, s2) -> {
            String time1 = s1.getStartTime() != null ? s1.getStartTime() : "00:00:00";
            String time2 = s2.getStartTime() != null ? s2.getStartTime() : "00:00:00";
            return time1.compareTo(time2);
        });
        
        scheduleAdapter.updateSchedule(filteredSchedule);
        
        if (filteredSchedule.isEmpty()) {
            showEmptyState(true);
        } else {
            showEmptyState(false);
        }
    }
    
    private void showLoading(boolean show) {
        progressLoading.setVisibility(show ? View.VISIBLE : View.GONE);
        scheduleRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    
    private void showEmptyState(boolean show) {
        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
        scheduleRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
} 