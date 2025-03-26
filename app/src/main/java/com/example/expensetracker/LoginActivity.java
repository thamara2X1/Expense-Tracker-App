package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.DatabaseHelper;
import com.example.expensetracker.User;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignup;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Initialize Views
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonLogin = findViewById(R.id.button_login);
        textViewSignup = findViewById(R.id.text_view_signup);

        // Login Button Click Listener
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        // Signup Text Click Listener
        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    private void performLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Basic validation
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        // Check user credentials
        User user = databaseHelper.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            // Successful login
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

            // Move to Dashboard
            Intent dashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
            dashboardIntent.putExtra("USER_ID", user.getId());
            startActivity(dashboardIntent);
            finish();
        } else {
            // Login failed
            Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
        }
    }
}