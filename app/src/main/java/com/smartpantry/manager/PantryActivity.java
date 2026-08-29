package com.smartpantry.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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

        databaseHelper = new DatabaseHelper(this);
        pantryAdapter = new PantryAdapter();

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
}
