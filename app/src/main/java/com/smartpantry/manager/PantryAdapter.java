package com.smartpantry.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final List<PantryItem> pantryItems = new ArrayList<>();
    private final DecimalFormat quantityFormat = new DecimalFormat("0.##");

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);

        holder.textIngredientName.setText(item.getName());

        String quantity = quantityFormat.format(item.getQuantity());
        String amount = holder.itemView.getContext().getString(
                R.string.pantry_item_amount,
                quantity,
                item.getUnit()
        );
        holder.textIngredientAmount.setText(amount);
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public void setItems(List<PantryItem> items) {
        pantryItems.clear();
        pantryItems.addAll(items);
        notifyDataSetChanged();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView textIngredientName;
        TextView textIngredientAmount;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textIngredientName = itemView.findViewById(R.id.textIngredientName);
            textIngredientAmount = itemView.findViewById(R.id.textIngredientAmount);
        }
    }
}
