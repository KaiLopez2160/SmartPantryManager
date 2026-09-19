package com.smartpantry.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;
    private RecyclerView recyclerSuggestedRecipes;
    private LinearLayout emptySuggestedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recyclerSuggestedRecipes = findViewById(R.id.recyclerSuggestedRecipes);
        emptySuggestedRecipes = findViewById(R.id.emptySuggestedRecipes);

        databaseHelper = new DatabaseHelper(this);

        recipeAdapter = new RecipeAdapter(recipe -> {
            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    RecipeDetailActivity.class
            );
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
            startActivity(intent);
        });

        recyclerSuggestedRecipes.setLayoutManager(new LinearLayoutManager(this));
        recyclerSuggestedRecipes.setAdapter(recipeAdapter);

        loadSuggestedRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        List<Recipe> recipes = databaseHelper.getSuggestedRecipes();
        recipeAdapter.setRecipes(recipes);

        if (recipes.isEmpty()) {
            emptySuggestedRecipes.setVisibility(View.VISIBLE);
            recyclerSuggestedRecipes.setVisibility(View.GONE);
        } else {
            emptySuggestedRecipes.setVisibility(View.GONE);
            recyclerSuggestedRecipes.setVisibility(View.VISIBLE);
        }
    }
}
