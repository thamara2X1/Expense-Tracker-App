package com.example.expensetracker;

public class Expense {
    private int id;
    private int userId;
    private double amount;
    private String category;
    private String description;
    private long timestamp; // Timestamp is now long
    private String date;

    // Updated constructor to include timestamp as long
    public Expense(int userId, double amount, String category, String description, String date, long timestamp) {
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        this.timestamp = timestamp;
    }

    // Constructor for getting all expenses from database
    public Expense(int id, int userId, double amount, String category, String description, long timestamp) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public long getTimestamp() { return timestamp; }
}
