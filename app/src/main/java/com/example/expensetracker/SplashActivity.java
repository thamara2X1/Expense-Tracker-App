package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Make the activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        // Use Handler to move to Login screen after splash duration
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Move to Login Activity
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish(); // Close splash activity
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}