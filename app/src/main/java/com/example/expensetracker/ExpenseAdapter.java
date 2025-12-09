package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private Context context;
    private List<Expense> expenseList;
    private OnExpenseClickListener listener;
    private NumberFormat currencyFormat;

    public interface OnExpenseClickListener {
        void onExpenseClick(int position);
        void onExpenseDelete(int position);
    }

    public ExpenseAdapter(Context context, List<Expense> expenseList, OnExpenseClickListener listener) {
        this.context = context;
        this.expenseList = expenseList;
        this.listener = listener;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        // Set data
        holder.textViewCategory.setText(expense.getCategory());
        holder.textViewDescription.setText(expense.getDescription());
        holder.textViewDate.setText(expense.getDate());

        // Format and set amount
        String formattedAmount = currencyFormat.format(Math.abs(expense.getAmount()));
        holder.textViewAmount.setText(formattedAmount);

        // Set colors based on income or expense
        if (expense.isIncome()) {
            holder.textViewAmount.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.imageViewIcon.setImageResource(android.R.drawable.arrow_down_float);
            holder.imageViewIcon.setColorFilter(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
        } else {
            holder.textViewAmount.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.imageViewIcon.setImageResource(android.R.drawable.arrow_up_float);
            holder.imageViewIcon.setColorFilter(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        // Click listeners
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onExpenseClick(holder.getAdapterPosition());
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onExpenseDelete(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageViewIcon;
        TextView textViewCategory;
        TextView textViewDescription;
        TextView textViewAmount;
        TextView textViewDate;
        ImageButton buttonDelete;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view_expense_item);
            imageViewIcon = itemView.findViewById(R.id.image_view_icon);
            textViewCategory = itemView.findViewById(R.id.text_view_category);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewAmount = itemView.findViewById(R.id.text_view_amount);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}