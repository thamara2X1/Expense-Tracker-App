package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "ExpenseTracker.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_EXPENSES = "expenses";

    // Expenses Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_USER_ID + " INTEGER," +
                KEY_AMOUNT + " REAL," +
                KEY_CATEGORY + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_DATE + " TEXT" +
                ")";
        db.execSQL(CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // Add new expense
    public long addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, expense.getUserId());
        values.put(KEY_AMOUNT, expense.getAmount());
        values.put(KEY_CATEGORY, expense.getCategory());
        values.put(KEY_DESCRIPTION, expense.getDescription());
        values.put(KEY_DATE, expense.getDate());

        long id = db.insert(TABLE_EXPENSES, null, values);
        db.close();
        return id;
    }

    // Get all expenses for a user
    public List<Expense> getAllExpenses(int userId) {
        List<Expense> expenseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES +
                " WHERE " + KEY_USER_ID + " = " + userId +
                " ORDER BY " + KEY_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                expense.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_USER_ID)));
                expense.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_AMOUNT)));
                expense.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                expense.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
                expense.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenseList;
    }

    // Get single expense
    public Expense getExpense(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPENSES,
                new String[]{KEY_ID, KEY_USER_ID, KEY_AMOUNT, KEY_CATEGORY, KEY_DESCRIPTION, KEY_DATE},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        Expense expense = null;
        if (cursor != null && cursor.moveToFirst()) {
            expense = new Expense();
            expense.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            expense.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_USER_ID)));
            expense.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_AMOUNT)));
            expense.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)));
            expense.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
            expense.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
            cursor.close();
        }

        db.close();
        return expense;
    }

    // Update expense
    public int updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, expense.getUserId());
        values.put(KEY_AMOUNT, expense.getAmount());
        values.put(KEY_CATEGORY, expense.getCategory());
        values.put(KEY_DESCRIPTION, expense.getDescription());
        values.put(KEY_DATE, expense.getDate());

        int rowsAffected = db.update(TABLE_EXPENSES, values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(expense.getId())});
        db.close();
        return rowsAffected;
    }

    // Delete expense
    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Get expense count
    public int getExpenseCount(int userId) {
        String countQuery = "SELECT * FROM " + TABLE_EXPENSES +
                " WHERE " + KEY_USER_ID + " = " + userId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}