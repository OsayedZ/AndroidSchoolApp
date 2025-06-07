package com.example.androidschoolapp.activities.common;

import android.os.Bundle;

import com.example.androidschoolapp.R;

public class MainActivity extends BaseActivity {

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_main, R.id.main);
    }
}