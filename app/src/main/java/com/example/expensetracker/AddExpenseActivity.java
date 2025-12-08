package com.example.expensetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText editTextAmount, editTextDescription, editTextDate;
    private Spinner spinnerCategory;
    private RadioGroup radioGroupType;
    private RadioButton radioIncome, radioExpense;
    private Button buttonSave, buttonCancel;
    private ImageButton buttonBack;
    private DatabaseHelper databaseHelper;
    private int currentUserId;

    // Categories
    private String[] expenseCategories = {
            "Food & Dining",
            "Transportation",
            "Shopping",
            "Entertainment",
            "Bills & Utilities",
            "Healthcare",
            "Education",
            "Travel",
            "Other"
    };

    private String[] incomeCategories = {
            "Salary",
            "Business",
            "Investment",
            "Gift",
            "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Get User ID from Intent
        currentUserId = getIntent().getIntExtra("USER_ID", 1);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Initialize Views
        initializeViews();

        // Set current date
        setCurrentDate();

        // Setup category spinner with default expense categories
        setupCategorySpinner(expenseCategories);

        // Setup listeners
        setupListeners();
    }

    private void initializeViews() {
        editTextAmount = findViewById(R.id.edit_text_amount);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextDate = findViewById(R.id.edit_text_date);
        spinnerCategory = findViewById(R.id.spinner_category);
        radioGroupType = findViewById(R.id.radio_group_type);
        radioIncome = findViewById(R.id.radio_income);
        radioExpense = findViewById(R.id.radio_expense);
        buttonSave = findViewById(R.id.button_save);
        buttonCancel = findViewById(R.id.button_cancel);
        buttonBack = findViewById(R.id.button_back);
    }

    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        editTextDate.setText(sdf.format(new Date()));
    }

    private void setupCategorySpinner(String[] categories) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void setupListeners() {
        // Back button
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Radio group listener to change categories
        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_income) {
                    setupCategorySpinner(incomeCategories);
                } else {
                    setupCategorySpinner(expenseCategories);
                }
            }
        });

        // Save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });

        // Cancel button
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveExpense() {
        // Get values
        String amountStr = editTextAmount.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        boolean isIncome = radioIncome.isChecked();

        // Validate inputs
        if (amountStr.isEmpty()) {
            editTextAmount.setError("Please enter amount");
            editTextAmount.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            editTextDescription.setError("Please enter description");
            editTextDescription.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            editTextDate.setError("Please enter date");
            editTextDate.requestFocus();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);

            // Make amount negative for expenses, positive for income
            if (!isIncome) {
                amount = -Math.abs(amount);
            } else {
                amount = Math.abs(amount);
            }

            // Create Expense object
            Expense expense = new Expense();
            expense.setUserId(currentUserId);
            expense.setAmount(amount);
            expense.setCategory(category);
            expense.setDescription(description);
            expense.setDate(date);

            // Save to database
            long result = databaseHelper.addExpense(expense);

            if (result > 0) {
                Toast.makeText(this, "Transaction saved successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Return to dashboard
            } else {
                Toast.makeText(this, "Failed to save transaction", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            editTextAmount.setError("Invalid amount");
            editTextAmount.requestFocus();
        }
    }
}