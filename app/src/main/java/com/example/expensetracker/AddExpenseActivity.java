package com.example.expensetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText editTextAmount, editTextDescription, editTextDate;
    private RadioGroup radioGroupTransactionType;
    private RadioButton radioButtonExpense, radioButtonIncome;
    private Button buttonSave;

    private DatabaseHelper databaseHelper;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Get User ID from Intent
        currentUserId = getIntent().getIntExtra("USER_ID", -1);
        if (currentUserId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Initialize Views
        initializeViews();

        // Setup Save Button Listener
        setupSaveButtonListener();
    }

    private void initializeViews() {
        editTextAmount = findViewById(R.id.edit_text_amount);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextDate = findViewById(R.id.edit_text_date);

        radioGroupTransactionType = findViewById(R.id.radio_group_transaction_type);
        radioButtonExpense = findViewById(R.id.radio_button_expense);
        radioButtonIncome = findViewById(R.id.radio_button_income);

        buttonSave = findViewById(R.id.button_save);

        // Set default date to current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        editTextDate.setText(dateFormat.format(new Date()));
    }

    private void setupSaveButtonListener() {
        buttonSave.setOnClickListener(v -> {
            if (validateInput()) {
                saveExpense();
            }
        });
    }

    private boolean validateInput() {
        // Validate Amount
        String amountStr = editTextAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            editTextAmount.setError("Amount is required");
            return false;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                editTextAmount.setError("Amount must be greater than zero");
                return false;
            }
        } catch (NumberFormatException e) {
            editTextAmount.setError("Invalid amount");
            return false;
        }

        // Validate Description
        String description = editTextDescription.getText().toString().trim();
        if (description.isEmpty()) {
            editTextDescription.setError("Description is required");
            return false;
        }

        // Validate Date
        String dateStr = editTextDate.getText().toString().trim();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dateFormat.parse(dateStr);
        } catch (ParseException e) {
            editTextDate.setError("Invalid date format. Use YYYY-MM-DD");
            return false;
        }

        return true;
    }

    private void saveExpense() {
        // Get input values
        double amount = Double.parseDouble(editTextAmount.getText().toString());
        String description = editTextDescription.getText().toString().trim();
        String dateStr = editTextDate.getText().toString().trim();

        // Determine transaction type (expense or income)
        int selectedTypeId = radioGroupTransactionType.getCheckedRadioButtonId();
        if (selectedTypeId == R.id.radio_button_expense) {
            amount = -Math.abs(amount);  // Expense is negative
        } else {
            amount = Math.abs(amount);   // Income is positive
        }

        // Get current timestamp
        long timestamp = System.currentTimeMillis();

        // Create Expense object with timestamp
        Expense expense = new Expense(currentUserId, amount, "General", description, dateStr, timestamp);

        // Save to database
        long result = databaseHelper.addExpense(expense);

        if (result != -1) {
            Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity and return to dashboard
        } else {
            Toast.makeText(this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
        }
    }

}
