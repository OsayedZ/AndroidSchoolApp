package com.example.androidschoolapp.utils;

import com.example.androidschoolapp.models.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateUtils {
    
    public static class DayItem {
        private int dayNumber;
        private String dayName;
        private boolean isToday;
        private boolean isSelected;
        
        public DayItem(int dayNumber, String dayName, boolean isToday) {
            this.dayNumber = dayNumber;
            this.dayName = dayName;
            this.isToday = isToday;
            this.isSelected = isToday; // Select today by default
        }
        
        // Getters and setters
        public int getDayNumber() { return dayNumber; }
        public String getDayName() { return dayName; }
        public boolean isToday() { return isToday; }
        public boolean isSelected() { return isSelected; }
        public void setSelected(boolean selected) { this.isSelected = selected; }
    }
    
    public static List<DayItem> getWeekDays() {
        List<DayItem> dayItems = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int todayDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        // Create day items
        String[] dayNames = {"SUN", "MON", "TUE", "WED", "THU"};
        
        for (int i = 1; i <= 5; i++) {
            String dayName = dayNames[i - 1];
            boolean isToday = (i == todayDayOfWeek);
            dayItems.add(new DayItem(i, dayName, isToday));
        }

        return dayItems;
    }
    
    public static int getTodayDayNumber() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    
    public static String getDayName(int dayNumber) {
        switch (dayNumber) {
            case 1: return "SUN";
            case 2: return "MON";
            case 3: return "TUE";
            case 4: return "WED";
            case 5: return "THU";
            default: return "";

        }
    }
    
    public static String getDayFullName(int dayNumber) {
        switch (dayNumber) {
            case 1: return "Sunday";
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            default: return "";
        }
    }
} 