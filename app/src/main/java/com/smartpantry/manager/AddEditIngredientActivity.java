package com.smartpantry.manager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText editIngredientName;
    private EditText editQuantity;
    private EditText editUnit;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        editIngredientName = findViewById(R.id.editIngredientName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        Button buttonSaveIngredient = findViewById(R.id.buttonSaveIngredient);

        databaseHelper = new DatabaseHelper(this);

        buttonSaveIngredient.setOnClickListener(view -> saveIngredient());
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

        PantryItem pantryItem = new PantryItem(name, quantity, unit);
        long result = databaseHelper.addPantryItem(pantryItem);

        if (result != -1) {
            Toast.makeText(this, R.string.ingredient_added, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.ingredient_save_error, Toast.LENGTH_SHORT).show();
        }
    }
}
