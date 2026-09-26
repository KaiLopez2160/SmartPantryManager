package com.smartpantry.manager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "pantry_item_id";

    private EditText editIngredientName;
    private EditText editQuantity;
    private EditText editUnit;
    private Button buttonSaveIngredient;
    private TextView textFormTitle;
    private DatabaseHelper databaseHelper;
    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        textFormTitle = findViewById(R.id.textFormTitle);
        editIngredientName = findViewById(R.id.editIngredientName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        buttonSaveIngredient = findViewById(R.id.buttonSaveIngredient);

        databaseHelper = new DatabaseHelper(this);
        itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);

        if (itemId != -1) {
            loadIngredient();
        }

        buttonSaveIngredient.setOnClickListener(view -> saveIngredient());
    }

    private void loadIngredient() {
        PantryItem pantryItem = databaseHelper.getPantryItem(itemId);

        if (pantryItem == null) {
            Toast.makeText(this, R.string.ingredient_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textFormTitle.setText(R.string.edit_ingredient_title);
        buttonSaveIngredient.setText(R.string.update_ingredient);
        editIngredientName.setText(pantryItem.getName());
        editQuantity.setText(String.valueOf(pantryItem.getQuantity()));
        editUnit.setText(pantryItem.getUnit());
    }

    private void saveIngredient() {
        String name = editIngredientName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();

        if (name.isEmpty()) {
            editIngredientName.setError(getString(R.string.error_ingredient_name));
            editIngredientName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            editQuantity.setError(getString(R.string.error_quantity_required));
            editQuantity.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException exception) {
            editQuantity.setError(getString(R.string.error_quantity_number));
            editQuantity.requestFocus();
            return;
        }

        if (Double.isNaN(quantity) || Double.isInfinite(quantity)) {
            editQuantity.setError(getString(R.string.error_quantity_number));
            editQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {
            editQuantity.setError(getString(R.string.error_quantity_positive));
            editQuantity.requestFocus();
            return;
        }

        if (unit.isEmpty()) {
            editUnit.setError(getString(R.string.error_unit));
            editUnit.requestFocus();
            return;
        }

        if (itemId == -1) {
            addIngredient(name, quantity, unit);
        } else {
            updateIngredient(name, quantity, unit);
        }
    }

    private void addIngredient(String name, double quantity, String unit) {
        PantryItem pantryItem = new PantryItem(name, quantity, unit);
        long result = databaseHelper.addPantryItem(pantryItem);

        if (result != -1) {
            Toast.makeText(this, R.string.ingredient_added, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.ingredient_save_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateIngredient(String name, double quantity, String unit) {
        PantryItem pantryItem = new PantryItem(itemId, name, quantity, unit);
        int rowsUpdated = databaseHelper.updatePantryItem(pantryItem);

        if (rowsUpdated > 0) {
            Toast.makeText(this, R.string.ingredient_updated, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.ingredient_save_error, Toast.LENGTH_SHORT).show();
        }
    }
}
