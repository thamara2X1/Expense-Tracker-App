package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;
import java.util.ArrayList;

import com.example.expensetracker.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ExpenseTrackerDB";
    private static final int DATABASE_VERSION = 2;

    // User Table
    private static final String TABLE_USERS = "users";
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Expense table columns
    private static final String COLUMN_EXPENSE_ID = "id";
    private static final String COLUMN_EXPENSE_USER_ID = "user_id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_DATE = "date";

    // Create table statements
    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USERNAME + " TEXT UNIQUE, "
                    + COLUMN_EMAIL + " TEXT, "
                    + COLUMN_PASSWORD + " TEXT)";

    private static final String CREATE_EXPENSES_TABLE =
            "CREATE TABLE " + TABLE_EXPENSES + "("
                    + COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_EXPENSE_USER_ID + " INTEGER, "
                    + COLUMN_AMOUNT + " REAL, "
                    + COLUMN_CATEGORY + " TEXT, "
                    + COLUMN_DESCRIPTION + " TEXT, "
                    + COLUMN_TIMESTAMP + " INTEGER, "
                    + COLUMN_DATE + " INTEGER, "
                    + "FOREIGN KEY(" + COLUMN_EXPENSE_USER_ID + ") REFERENCES "
                    + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        db.execSQL(CREATE_USERS_TABLE);

        // Create Expenses Table
        db.execSQL(CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // Method to add a new user
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());

        // Insert row
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    // Method to get user by email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_EMAIL, COLUMN_PASSWORD},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            cursor.close();
            return user;
        }
        return null;
    }

    // Method to add an expense
    public long addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_USER_ID, expense.getUserId());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        values.put(COLUMN_CATEGORY, expense.getCategory());
        values.put(COLUMN_DESCRIPTION, expense.getDescription());
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis());
        values.put(COLUMN_DATE, expense.getDate());

        long result = db.insert(TABLE_EXPENSES, null, values);
        db.close();
        return result;
    }

    // Method to get all expenses for a specific user
    public List<Expense> getAllExpenses(int userId) {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_EXPENSE_ID,
                COLUMN_EXPENSE_USER_ID,
                COLUMN_AMOUNT,
                COLUMN_CATEGORY,
                COLUMN_DESCRIPTION,
                COLUMN_TIMESTAMP
        };

        String selection = COLUMN_EXPENSE_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        Cursor cursor = db.query(
                TABLE_EXPENSES,    // The table to query
                projection,        // The columns to return
                selection,         // The columns for the WHERE clause
                selectionArgs,     // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                COLUMN_TIMESTAMP + " DESC"  // The sort order (most recent first)
        );

        // Iterate through all rows and create Expense objects
        while (cursor.moveToNext()) {
            Expense expense = new Expense(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPENSE_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPENSE_USER_ID)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)) // Get timestamp as long
            );
            expenses.add(expense);
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return expenses;
    }

}