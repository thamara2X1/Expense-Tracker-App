package com.example.expensetracker;

public class Expense {
    private int id;
    private int userId;
    private double amount;
    private String category;
    private String description;
    private long timestamp;

    // Constructor
    public Expense(int id, int userId, double amount, String category, String description, long timestamp) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Optional: Setters if needed
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

