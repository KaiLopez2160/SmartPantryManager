package com.smartpantry.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnPantryItemActionListener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final List<PantryItem> pantryItems = new ArrayList<>();
    private final DecimalFormat quantityFormat = new DecimalFormat("0.##");
    private final OnPantryItemActionListener listener;

    public PantryAdapter(OnPantryItemActionListener listener) {
        this.listener = listener;
    }

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

        Context context = holder.itemView.getContext();
        double displayQuantity = item.getQuantity();
        String displayUnit = item.getUnit();

        boolean compact = context.getSharedPreferences(
                SettingsActivity.PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getBoolean(SettingsActivity.KEY_COMPACT_UNITS, false);

        if (compact && displayQuantity >= 1000) {
            String unit = displayUnit.trim().toLowerCase(Locale.ROOT);

            if (unit.equals("g") || unit.equals("gram") || unit.equals("grams")) {
                displayQuantity /= 1000;
                displayUnit = "kg";
            } else if (unit.equals("ml") || unit.equals("millilitre")
                    || unit.equals("millilitres") || unit.equals("milliliter")
                    || unit.equals("milliliters")) {
                displayQuantity /= 1000;
                displayUnit = "L";
            }
        }

        String quantity = quantityFormat.format(displayQuantity);
        String amount = context.getString(
                R.string.pantry_item_amount, quantity, displayUnit);

        holder.textIngredientAmount.setText(amount);

        holder.buttonEdit.setOnClickListener(view -> listener.onEdit(item));
        holder.buttonDelete.setOnClickListener(view -> listener.onDelete(item));
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
        Button buttonEdit;
        Button buttonDelete;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textIngredientName = itemView.findViewById(R.id.textIngredientName);
            textIngredientAmount = itemView.findViewById(R.id.textIngredientAmount);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
