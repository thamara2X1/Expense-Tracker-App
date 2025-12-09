package com.example.expensetracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportsActivity extends AppCompatActivity {
    private ImageButton buttonBack;
    private TextView textViewTotalTransactions, textViewHighestExpense, textViewHighestIncome;
    private TextView textViewAvgExpense, textViewAvgIncome;
    private PieChart pieChartExpenses, pieChartIncome;
    private BarChart barChartComparison;
    private CardView cardViewNoData;
    private DatabaseHelper databaseHelper;
    private int currentUserId;
    private NumberFormat currencyFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // Get User ID from Intent
        currentUserId = getIntent().getIntExtra("USER_ID", 1);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);
        currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());

        // Initialize Views
        initializeViews();

        // Load Reports Data
        loadReportsData();

        // Setup listeners
        setupListeners();
    }

    private void initializeViews() {
        buttonBack = findViewById(R.id.button_back);
        textViewTotalTransactions = findViewById(R.id.text_view_total_transactions);
        textViewHighestExpense = findViewById(R.id.text_view_highest_expense);
        textViewHighestIncome = findViewById(R.id.text_view_highest_income);
        textViewAvgExpense = findViewById(R.id.text_view_avg_expense);
        textViewAvgIncome = findViewById(R.id.text_view_avg_income);
        pieChartExpenses = findViewById(R.id.pie_chart_expenses);
        pieChartIncome = findViewById(R.id.pie_chart_income);
        barChartComparison = findViewById(R.id.bar_chart_comparison);
        cardViewNoData = findViewById(R.id.card_view_no_data);
    }

    private void loadReportsData() {
        List<Expense> expenses = databaseHelper.getAllExpenses(currentUserId);

        if (expenses.isEmpty()) {
            // Show no data message
            cardViewNoData.setVisibility(View.VISIBLE);
            pieChartExpenses.setVisibility(View.GONE);
            pieChartIncome.setVisibility(View.GONE);
            barChartComparison.setVisibility(View.GONE);
            return;
        }

        cardViewNoData.setVisibility(View.GONE);

        // Separate expenses and income
        List<Expense> expenseList = new ArrayList<>();
        List<Expense> incomeList = new ArrayList<>();

        for (Expense expense : expenses) {
            if (expense.isIncome()) {
                incomeList.add(expense);
            } else {
                expenseList.add(expense);
            }
        }

        // Calculate statistics
        calculateStatistics(expenses, expenseList, incomeList);

        // Setup charts
        setupExpensePieChart(expenseList);
        setupIncomePieChart(incomeList);
        setupComparisonBarChart(expenseList, incomeList);
    }

    private void calculateStatistics(List<Expense> all, List<Expense> expenses, List<Expense> income) {
        // Total transactions
        textViewTotalTransactions.setText(String.valueOf(all.size()));

        // Highest expense
        double highestExpense = 0;
        for (Expense exp : expenses) {
            if (Math.abs(exp.getAmount()) > highestExpense) {
                highestExpense = Math.abs(exp.getAmount());
            }
        }
        textViewHighestExpense.setText(currencyFormat.format(highestExpense));

        // Highest income
        double highestIncome = 0;
        for (Expense inc : income) {
            if (inc.getAmount() > highestIncome) {
                highestIncome = inc.getAmount();
            }
        }
        textViewHighestIncome.setText(currencyFormat.format(highestIncome));

        // Average expense
        double totalExpense = 0;
        for (Expense exp : expenses) {
            totalExpense += Math.abs(exp.getAmount());
        }
        double avgExpense = expenses.isEmpty() ? 0 : totalExpense / expenses.size();
        textViewAvgExpense.setText(currencyFormat.format(avgExpense));

        // Average income
        double totalIncome = 0;
        for (Expense inc : income) {
            totalIncome += inc.getAmount();
        }
        double avgIncome = income.isEmpty() ? 0 : totalIncome / income.size();
        textViewAvgIncome.setText(currencyFormat.format(avgIncome));
    }

    private void setupExpensePieChart(List<Expense> expenses) {
        if (expenses.isEmpty()) {
            pieChartExpenses.setVisibility(View.GONE);
            return;
        }

        pieChartExpenses.setVisibility(View.VISIBLE);

        // Group by category
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double amount = Math.abs(expense.getAmount());
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
        }

        // Create pie entries
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expenses by Category");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return currencyFormat.format(value);
            }
        });

        pieChartExpenses.setData(data);
        pieChartExpenses.getDescription().setEnabled(false);
        pieChartExpenses.setCenterText("Expenses\nBy Category");
        pieChartExpenses.setCenterTextSize(14f);
        pieChartExpenses.setDrawHoleEnabled(true);
        pieChartExpenses.setHoleColor(Color.WHITE);
        pieChartExpenses.setTransparentCircleRadius(58f);
        pieChartExpenses.animateY(1000);
        pieChartExpenses.invalidate();
    }

    private void setupIncomePieChart(List<Expense> income) {
        if (income.isEmpty()) {
            pieChartIncome.setVisibility(View.GONE);
            return;
        }

        pieChartIncome.setVisibility(View.VISIBLE);

        // Group by category
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense inc : income) {
            String category = inc.getCategory();
            double amount = inc.getAmount();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
        }

        // Create pie entries
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Income by Category");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return currencyFormat.format(value);
            }
        });

        pieChartIncome.setData(data);
        pieChartIncome.getDescription().setEnabled(false);
        pieChartIncome.setCenterText("Income\nBy Category");
        pieChartIncome.setCenterTextSize(14f);
        pieChartIncome.setDrawHoleEnabled(true);
        pieChartIncome.setHoleColor(Color.WHITE);
        pieChartIncome.setTransparentCircleRadius(58f);
        pieChartIncome.animateY(1000);
        pieChartIncome.invalidate();
    }

    private void setupComparisonBarChart(List<Expense> expenses, List<Expense> income) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        // Calculate totals
        double totalExpense = 0;
        for (Expense exp : expenses) {
            totalExpense += Math.abs(exp.getAmount());
        }

        double totalIncome = 0;
        for (Expense inc : income) {
            totalIncome += inc.getAmount();
        }

        entries.add(new BarEntry(0f, (float) totalExpense));
        entries.add(new BarEntry(1f, (float) totalIncome));

        BarDataSet dataSet = new BarDataSet(entries, "Income vs Expenses");
        dataSet.setColors(new int[]{Color.rgb(244, 67, 54), Color.rgb(76, 175, 80)});
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData data = new BarData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return currencyFormat.format(value);
            }
        });

        barChartComparison.setData(data);
        barChartComparison.getDescription().setEnabled(false);
        barChartComparison.getXAxis().setEnabled(false);
        barChartComparison.getAxisRight().setEnabled(false);
        barChartComparison.getAxisLeft().setTextSize(10f);
        barChartComparison.getLegend().setEnabled(false);
        barChartComparison.setFitBars(true);
        barChartComparison.animateY(1000);
        barChartComparison.invalidate();
    }

    private void setupListeners() {
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}