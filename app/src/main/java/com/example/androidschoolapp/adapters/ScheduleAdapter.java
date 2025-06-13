package com.example.androidschoolapp.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.models.Subject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Subject> scheduleList;
    private Context context;

    public ScheduleAdapter(Context context, List<Subject> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }


    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule_class, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Subject subject = scheduleList.get(position);
        
        // Set subject name
        holder.subjectNameTextView.setText(subject.getName());
        
        // Set time range (start - end)
        String startTime = formatTime(subject.getStartTime());
        String endTime = formatTime(subject.getEndTime());
        holder.classTimeStartTextView.setText(startTime);
        holder.classTimeEndTextView.setText(endTime);

        // Set subject color accent
        int colorResId = getSubjectColor(subject.getName());
        int color = ContextCompat.getColor(context, colorResId);
        
        GradientDrawable accentDrawable = new GradientDrawable();
        accentDrawable.setShape(GradientDrawable.RECTANGLE);
        accentDrawable.setColor(color);
        accentDrawable.setCornerRadius(8f);
        holder.subjectAccentView.setBackground(accentDrawable);
        
        // Handle timeline indicator
        GradientDrawable indicatorDrawable = new GradientDrawable();
        indicatorDrawable.setShape(GradientDrawable.OVAL);
        indicatorDrawable.setColor(color);
        holder.timelineIndicator.setBackground(indicatorDrawable);
        
        // Check if this is the current/next class
        boolean isCurrentClass = isCurrentOrNextClass(subject);
        if (isCurrentClass) {
            holder.classStatusTextView.setVisibility(View.VISIBLE);
            holder.classStatusTextView.setText("NOW");
        } else {
            holder.classStatusTextView.setVisibility(View.GONE);
        }
        
        // Hide timeline line for last item
        if (position == scheduleList.size() - 1) {
            holder.timelineLineView.setVisibility(View.INVISIBLE);
        } else {
            holder.timelineLineView.setVisibility(View.VISIBLE);
        }
    }

    private int getSubjectColor(String subjectName) {
        return R.color.schedule_subject_default;
    }

    private String formatTime(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            return "N/A";
        }
        
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            Date time = inputFormat.parse(timeString);
            return outputFormat.format(time);
        } catch (ParseException e) {
            // Try alternative format
            try {
                SimpleDateFormat altFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                Date time = altFormat.parse(timeString);
                return outputFormat.format(time);
            } catch (ParseException ex) {
                return timeString; // Return original if parsing fails
            }
        }
    }

    private boolean isCurrentOrNextClass(Subject subject) {
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date startTime = format.parse(subject.getStartTime());
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(startTime);
            int startHour = startCal.get(Calendar.HOUR_OF_DAY);
            
            // Check if this class is happening now (within 1 hour window)
            return Math.abs(currentHour - startHour) <= 1;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateSchedule(List<Subject> newSchedule) {
        this.scheduleList = newSchedule;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView classTimeStartTextView;
        TextView classTimeEndTextView;
        TextView subjectNameTextView;
        TextView classStatusTextView;
        View timelineIndicator;
        View timelineLineView;
        View subjectAccentView;
        CardView classCard;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            classTimeStartTextView = itemView.findViewById(R.id.tv_class_time_start);
            classTimeEndTextView = itemView.findViewById(R.id.tv_class_time_end);

            subjectNameTextView = itemView.findViewById(R.id.tv_subject_name);
            classStatusTextView = itemView.findViewById(R.id.tv_class_status);
            timelineIndicator = itemView.findViewById(R.id.view_timeline_indicator);
            timelineLineView = itemView.findViewById(R.id.view_timeline_line);
            subjectAccentView = itemView.findViewById(R.id.view_subject_accent);
            classCard = itemView.findViewById(R.id.card_class);
        }
    }
} 