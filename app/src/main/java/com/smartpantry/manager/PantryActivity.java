package com.smartpantry.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private PantryAdapter pantryAdapter;
    private RecyclerView recyclerPantry;
    private LinearLayout emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        recyclerPantry = findViewById(R.id.recyclerPantry);
        emptyState = findViewById(R.id.emptyState);

        Button buttonAddIngredient = findViewById(R.id.buttonAddIngredient);

        NavigationHelper.setup(this, R.id.navPantry);

        databaseHelper = new DatabaseHelper(this);

        pantryAdapter = new PantryAdapter(new PantryAdapter.OnPantryItemActionListener() {
            @Override
            public void onEdit(PantryItem item) {
                openEditIngredient(item);
            }

            @Override
            public void onDelete(PantryItem item) {
                showDeleteConfirmation(item);
            }
        });

        recyclerPantry.setLayoutManager(new LinearLayoutManager(this));
        recyclerPantry.setAdapter(pantryAdapter);

        buttonAddIngredient.setOnClickListener(view -> {
            Intent intent = new Intent(PantryActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }

    private void loadPantryItems() {
        List<PantryItem> pantryItems = databaseHelper.getAllPantryItems();
        pantryAdapter.setItems(pantryItems);

        if (pantryItems.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerPantry.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerPantry.setVisibility(View.VISIBLE);
        }
    }

    private void openEditIngredient(PantryItem item) {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    private void showDeleteConfirmation(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_ingredient_title)
                .setMessage(getString(R.string.delete_ingredient_message, item.getName()))
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteIngredient(item))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void deleteIngredient(PantryItem item) {
        int rowsDeleted = databaseHelper.deletePantryItem(item.getId());

        if (rowsDeleted > 0) {
            Toast.makeText(this, R.string.ingredient_deleted, Toast.LENGTH_SHORT).show();
            loadPantryItems();
        } else {
            Toast.makeText(this, R.string.ingredient_delete_error, Toast.LENGTH_SHORT).show();
        }
    }
}
