package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.expensetracker.DatabaseHelper;
import com.example.expensetracker.Expense;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {
    private TextView textViewTotalBalance, textViewTotalIncome, textViewTotalExpense;
    private CardView cardViewAddExpense, cardViewExpenseList, cardViewReports, cardViewSettings;
    private ImageButton buttonLogout;
    private DatabaseHelper databaseHelper;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Get User ID from Intent
        currentUserId = getIntent().getIntExtra("USER_ID", -1);
        if (currentUserId == -1) {
            // Redirect to login if no user ID
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Initialize Views
        initializeViews();

        // Load Dashboard Data
        loadDashboardData();

        // Setup Click Listeners
        setupClickListeners();
    }

    private void initializeViews() {
        textViewTotalBalance = findViewById(R.id.text_view_total_balance);
        textViewTotalIncome = findViewById(R.id.text_view_total_income);
        textViewTotalExpense = findViewById(R.id.text_view_total_expense);

        cardViewAddExpense = findViewById(R.id.card_view_add_expense);
        cardViewExpenseList = findViewById(R.id.card_view_expense_list);
        cardViewReports = findViewById(R.id.card_view_reports);
        cardViewSettings = findViewById(R.id.card_view_settings);

        buttonLogout = findViewById(R.id.button_logout);
    }

    private void loadDashboardData() {
        // Fetch expenses for current user
        List<Expense> expenses = databaseHelper.getAllExpenses(currentUserId);

        // Calculate totals
        double totalExpense = calculateTotalExpense(expenses);
        double totalIncome = calculateTotalIncome(expenses);
        double totalBalance = totalIncome - totalExpense;

        // Format currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());

        // Set TextViews
        textViewTotalBalance.setText(currencyFormat.format(totalBalance));
        textViewTotalIncome.setText(currencyFormat.format(totalIncome));
        textViewTotalExpense.setText(currencyFormat.format(totalExpense));
    }

    private double calculateTotalExpense(List<Expense> expenses) {
        double total = 0;
        for (Expense expense : expenses) {
            // Assuming negative amounts are expenses
            if (expense.getAmount() < 0) {
                total += Math.abs(expense.getAmount());
            }
        }
        return total;
    }

    private double calculateTotalIncome(List<Expense> expenses) {
        double total = 0;
        for (Expense expense : expenses) {
            // Assuming positive amounts are income
            if (expense.getAmount() > 0) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    private void setupClickListeners() {
        // Add Expense
//        cardViewAddExpense.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent addExpenseIntent = new Intent(DashboardActivity.this, AddExpenseActivity.class);
//                addExpenseIntent.putExtra("USER_ID", currentUserId);
//                startActivity(addExpenseIntent);
//            }
//        });

        // Expense List
//        cardViewExpenseList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent expenseListIntent = new Intent(DashboardActivity.this, ExpenseListActivity.class);
//                expenseListIntent.putExtra("USER_ID", currentUserId);
//                startActivity(expenseListIntent);
//            }
//        });

        // Reports
//        cardViewReports.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent reportsIntent = new Intent(DashboardActivity.this, ReportsActivity.class);
//                reportsIntent.putExtra("USER_ID", currentUserId);
//                startActivity(reportsIntent);
//            }
//        });

        // Settings
//        cardViewSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent settingsIntent = new Intent(DashboardActivity.this, SettingsActivity.class);
//                settingsIntent.putExtra("USER_ID", currentUserId);
//                startActivity(settingsIntent);
//            }
//        });

        // Logout
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to Login Screen
                Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload dashboard data when returning to this activity
        loadDashboardData();
    }
}