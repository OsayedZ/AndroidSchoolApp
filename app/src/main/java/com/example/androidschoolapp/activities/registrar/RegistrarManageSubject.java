package com.example.androidschoolapp.activities.registrar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Class;
import com.example.androidschoolapp.models.Subject;
import com.example.androidschoolapp.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegistrarManageSubject extends BaseActivity {

    private EditText input_subject_name;
    private Spinner spn_teacher;

    private TextInputEditText input_start_time, input_end_time;

    private AutoCompleteTextView input_day;

    private Button btn_add_subject;

    private Gson gson = new Gson();

    private Subject selected_subject;

    public RegistrarManageSubject() {
        super("Manage Subject");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_registrar_manage_subject, R.id.registrar_manage_subject);
        initializeViews();
        handleEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean m = getIntent().getBooleanExtra("Edit", false);

        load(m);

        if (m) {
            btn_add_subject.setText("Edit");
            btn_add_subject.setOnClickListener(e -> editSubject());

            String json = getIntent().getStringExtra("Subject");
            if (!json.isEmpty()) {
                Subject s = gson.fromJson(json, Subject.class);
                selected_subject = s;

                // Set value for name
                input_subject_name.setText(selected_subject.getName());

                // Set value for spinner


                // Set value for the day
                String day = convertNumberToDay(selected_subject.getDay());
                input_day.setText(day, false);

                // Set value for the start time
                input_start_time.setText(selected_subject.getStartTime());

                // Set value for the end time
                input_end_time.setText(selected_subject.getEndTime());
            }
        } else {
            btn_add_subject.setText("Add");
            btn_add_subject.setOnClickListener(e -> addSubject());
        }
    }

    private void initializeViews() {
        input_subject_name = findViewById(R.id.input_subject_name);
        spn_teacher = findViewById(R.id.spn_teacher);
        input_day = findViewById(R.id.input_day);
        input_start_time = findViewById(R.id.input_start_time);
        input_end_time = findViewById(R.id.input_end_time);
        btn_add_subject = findViewById(R.id.btn_add_subject);

        String[] days = {
                "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
        };

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                days
        );

        input_day.setAdapter(dayAdapter);
        input_day.setOnClickListener(v -> input_day.showDropDown());

        View.OnClickListener timeClickListener1 = view -> {
            final boolean isStart = view.getId() == R.id.input_start_time;

            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(hour)
                    .setMinute(minute)
                    .setTitleText(isStart ? "Select Start Time" : "Select End Time")
                    .build();

            picker.show(getSupportFragmentManager(), isStart ? "START_TIME" : "END_TIME");

            picker.addOnPositiveButtonClickListener(dialog -> {
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s",
                        (picker.getHour() % 12 == 0 ? 12 : picker.getHour() % 12),
                        picker.getMinute(),
                        picker.getHour() >= 12 ? "PM" : "AM");

                if (isStart) {
                    input_start_time.setText(formattedTime);
                } else {
                    input_start_time.setText(formattedTime);
                }
            });
        };

        View.OnClickListener timeClickListener2 = view -> {
            final boolean isStart = view.getId() == R.id.input_start_time;

            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(hour)
                    .setMinute(minute)
                    .setTitleText(isStart ? "Select Start Time" : "Select End Time")
                    .build();

            picker.show(getSupportFragmentManager(), isStart ? "START_TIME" : "END_TIME");

            picker.addOnPositiveButtonClickListener(dialog -> {
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s",
                        (picker.getHour() % 12 == 0 ? 12 : picker.getHour() % 12),
                        picker.getMinute(),
                        picker.getHour() >= 12 ? "PM" : "AM");

                if (isStart) {
                    input_end_time.setText(formattedTime);
                } else {
                    input_end_time.setText(formattedTime);
                }
            });
        };

        input_start_time.setOnClickListener(timeClickListener1);
        input_end_time.setOnClickListener(timeClickListener2);
    }

    private void load(boolean edit) {
        apiClient.getTeachers(new ApiClient.DataCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                ArrayAdapter<User> adapter = new ArrayAdapter<>(
                        RegistrarManageSubject.this,
                        android.R.layout.simple_spinner_item,
                        result
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_teacher.setAdapter(adapter);

                if(edit) {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        User user = adapter.getItem(i);
                        if (user.getId() == selected_subject.getTeacherId()) {
                            spn_teacher.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void handleEvents() {
        btn_add_subject.setOnClickListener(e -> addSubject());
    }

    private void addSubject() {
        String subjectName = input_subject_name.getText().toString();
        User selectedTeacher = (User)spn_teacher.getSelectedItem();
        int teacherID = selectedTeacher.getId();
        String teacherName = selectedTeacher.getName();
        String selectedDay = input_day.getText().toString();
        String startTime = input_start_time.getText().toString().trim();
        String endTime = input_end_time.getText().toString().trim();

        if (subjectName.isEmpty() || selectedDay.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            showToast("Please Enter all fields");
            return;
        }

        int dayNumber = convertDayToNumber(selectedDay);

        apiClient.addSubject(new Subject(0, subjectName, teacherName, teacherID, startTime, endTime, dayNumber), new ApiClient.DataCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("Subject is added Successfully!");
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    private int convertDayToNumber(String day) {
        if (day == null) return -1;

        switch (day.toLowerCase(Locale.ROOT)) {
            case "sunday":
                return 1;
            case "monday":
                return 2;
            case "tuesday":
                return 3;
            case "wednesday":
                return 4;
            case "thursday":
                return 5;
            case "friday":
                return 6;
            case "saturday":
                return 7;
            default:
                return -1;
        }
    }

    private String convertNumberToDay(int number) {
        switch (number) {
            case 1: return "Sunday";
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            case 6: return "Friday";
            case 7: return "Saturday";
            default: return "Invalid day";
        }
    }

    private void editSubject() {
        String subjectName = input_subject_name.getText().toString();
        User selectedTeacher = (User)spn_teacher.getSelectedItem();
        int teacherID = selectedTeacher.getId();
        String teacherName = selectedTeacher.getName();
        String selectedDay = input_day.getText().toString();
        String startTime = input_start_time.getText().toString().trim();
        String endTime = input_end_time.getText().toString().trim();

        if (subjectName.isEmpty() || selectedDay.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            showToast("Please Enter all fields");
            return;
        }

        int dayNumber = convertDayToNumber(selectedDay);

        apiClient.editSubject(new Subject(selected_subject.getId(),subjectName, teacherName, teacherID, startTime, endTime, dayNumber), new ApiClient.DataCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("Subject updated Successfully!");
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });

    }

}