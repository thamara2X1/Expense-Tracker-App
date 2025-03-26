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

public class SignupActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonSignup;
    private TextView textViewLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Initialize Views
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        buttonSignup = findViewById(R.id.button_signup);
        textViewLogin = findViewById(R.id.text_view_login);

        // Signup Button Click Listener
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignup();
            }
        });

        // Login Text Click Listener
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private void performSignup() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Validation
        if (username.isEmpty()) {
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
            return;
        }

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

        if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            editTextConfirmPassword.requestFocus();
            return;
        }

        // Create User Object
        User newUser = new User(0, username, email, password);

        // Attempt to add user to database
        long result = databaseHelper.addUser(newUser);
        if (result > 0) {
            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();

            // Move to Login Screen
            Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {
            Toast.makeText(this, "Signup Failed. Email might already exist.", Toast.LENGTH_SHORT).show();
        }
    }
}