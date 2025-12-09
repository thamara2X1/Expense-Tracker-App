package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseListActivity extends AppCompatActivity implements ExpenseAdapter.OnExpenseClickListener {
    private RecyclerView recyclerViewExpenses;
    private ExpenseAdapter expenseAdapter;
    private TextView textViewEmptyState;
    private ImageButton buttonBack;
    private DatabaseHelper databaseHelper;
    private int currentUserId;
    private List<Expense> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        // Get User ID from Intent
        currentUserId = getIntent().getIntExtra("USER_ID", 1);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Initialize Views
        initializeViews();

        // Load expenses
        loadExpenses();

        // Setup listeners
        setupListeners();
    }

    private void initializeViews() {
        recyclerViewExpenses = findViewById(R.id.recycler_view_expenses);
        textViewEmptyState = findViewById(R.id.text_view_empty_state);
        buttonBack = findViewById(R.id.button_back);

        // Setup RecyclerView
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewExpenses.setHasFixedSize(true);
    }

    private void loadExpenses() {
        expenseList = databaseHelper.getAllExpenses(currentUserId);

        if (expenseList.isEmpty()) {
            recyclerViewExpenses.setVisibility(View.GONE);
            textViewEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerViewExpenses.setVisibility(View.VISIBLE);
            textViewEmptyState.setVisibility(View.GONE);

            expenseAdapter = new ExpenseAdapter(this, expenseList, this);
            recyclerViewExpenses.setAdapter(expenseAdapter);
        }
    }

    private void setupListeners() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onExpenseClick(int position) {
        // Navigate to edit expense (future feature)
        Expense expense = expenseList.get(position);
        Toast.makeText(this, "Edit feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExpenseDelete(int position) {
        final Expense expense = expenseList.get(position);

        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Transaction")
                .setMessage("Are you sure you want to delete this transaction?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete from database
                        databaseHelper.deleteExpense(expense.getId());

                        // Remove from list and update adapter
                        expenseList.remove(position);
                        expenseAdapter.notifyItemRemoved(position);
                        expenseAdapter.notifyItemRangeChanged(position, expenseList.size());

                        // Show empty state if list is empty
                        if (expenseList.isEmpty()) {
                            recyclerViewExpenses.setVisibility(View.GONE);
                            textViewEmptyState.setVisibility(View.VISIBLE);
                        }

                        Toast.makeText(ExpenseListActivity.this, "Transaction deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload expenses when returning to this activity
        loadExpenses();
    }
}