package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {
    private ImageButton buttonBack;
    private Switch switchDarkMode, switchNotifications;
    private LinearLayout layoutClearData, layoutAbout, layoutPrivacy, layoutHelp;
    private TextView textViewVersion, textViewCurrency;
    private DatabaseHelper databaseHelper;
    private SharedPreferences preferences;
    private int currentUserId;

    // SharedPreferences keys
    private static final String PREFS_NAME = "ExpenseTrackerPrefs";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_CURRENCY = "currency";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get User ID from Intent
        currentUserId = getIntent().getIntExtra("USER_ID", 1);

        // Initialize
        databaseHelper = new DatabaseHelper(this);
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Initialize Views
        initializeViews();

        // Load Settings
        loadSettings();

        // Setup Listeners
        setupListeners();
    }

    private void initializeViews() {
        buttonBack = findViewById(R.id.button_back);
        switchDarkMode = findViewById(R.id.switch_dark_mode);
        switchNotifications = findViewById(R.id.switch_notifications);
        layoutClearData = findViewById(R.id.layout_clear_data);
        layoutAbout = findViewById(R.id.layout_about);
        layoutPrivacy = findViewById(R.id.layout_privacy);
        layoutHelp = findViewById(R.id.layout_help);
        textViewVersion = findViewById(R.id.text_view_version);
        textViewCurrency = findViewById(R.id.text_view_currency);

        // Set app version
        textViewVersion.setText("Version 1.0.0");
    }

    private void loadSettings() {
        // Load Dark Mode preference
        boolean darkMode = preferences.getBoolean(KEY_DARK_MODE, false);
        switchDarkMode.setChecked(darkMode);

        // Load Notifications preference
        boolean notifications = preferences.getBoolean(KEY_NOTIFICATIONS, true);
        switchNotifications.setChecked(notifications);

        // Load Currency preference
        String currency = preferences.getString(KEY_CURRENCY, "USD");
        textViewCurrency.setText(currency);
    }

    private void setupListeners() {
        // Back button
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Dark Mode Switch
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save preference
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_DARK_MODE, isChecked);
                editor.apply();

                // Apply dark mode
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                Toast.makeText(SettingsActivity.this, "Dark mode " + (isChecked ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
            }
        });

        // Notifications Switch
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save preference
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_NOTIFICATIONS, isChecked);
                editor.apply();

                Toast.makeText(SettingsActivity.this, "Notifications " + (isChecked ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
            }
        });

        // Currency Selection
        textViewCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyDialog();
            }
        });

        // Clear Data
        layoutClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearDataDialog();
            }
        });

        // About
        layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });

        // Privacy Policy
        layoutPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrivacyDialog();
            }
        });

        // Help & Support
        layoutHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpDialog();
            }
        });
    }

    private void showCurrencyDialog() {
        final String[] currencies = {"USD - US Dollar", "EUR - Euro", "GBP - British Pound",
                "JPY - Japanese Yen", "INR - Indian Rupee", "AUD - Australian Dollar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Currency");
        builder.setItems(currencies, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedCurrency = currencies[which].substring(0, 3);

                // Save preference
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(KEY_CURRENCY, selectedCurrency);
                editor.apply();

                // Update UI
                textViewCurrency.setText(selectedCurrency);

                Toast.makeText(SettingsActivity.this, "Currency changed to " + selectedCurrency, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void showClearDataDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Clear All Data")
                .setMessage("Are you sure you want to delete all your transactions? This action cannot be undone.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clear all expenses for current user
                        clearUserData();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void clearUserData() {
        // Get all expenses
        java.util.List<Expense> expenses = databaseHelper.getAllExpenses(currentUserId);

        // Delete each expense
        for (Expense expense : expenses) {
            databaseHelper.deleteExpense(expense.getId());
        }

        Toast.makeText(this, "All data cleared successfully", Toast.LENGTH_SHORT).show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About Expense Tracker")
                .setMessage("Expense Tracker v1.0.0\n\n" +
                        "A simple and powerful expense tracking application to help you manage your finances.\n\n" +
                        "Features:\n" +
                        "• Track income and expenses\n" +
                        "• View detailed reports\n" +
                        "• Categorize transactions\n" +
                        "• Visual analytics\n\n" +
                        "Developed with VTPCoding")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showPrivacyDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Privacy Policy")
                .setMessage("Privacy Policy\n\n" +
                        "Your data privacy is important to us.\n\n" +
                        "• All data is stored locally on your device\n" +
                        "• We do not collect or share your personal information\n" +
                        "• No internet connection required\n" +
                        "• Your financial data remains private\n\n" +
                        "By using this app, you agree to these terms.")
                .setPositiveButton("I Understand", null)
                .show();
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Help & Support")
                .setMessage("How to use Expense Tracker:\n\n" +
                        "1. Dashboard: View your financial overview\n\n" +
                        "2. Add Transaction: Tap the red card to add income or expenses\n\n" +
                        "3. View List: See all your transactions and delete if needed\n\n" +
                        "4. Reports: View charts and statistics\n\n" +
                        "5. Settings: Customize your preferences\n\n" +
                        "For more help, contact: support@vininduvtph@gmail.com")
                .setPositiveButton("Got it", null)
                .show();
    }
}