package com.smartpantry.manager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView textRecipeName = findViewById(R.id.textRecipeName);
        TextView textIngredients = findViewById(R.id.textIngredients);
        TextView textMethod = findViewById(R.id.textMethod);

        databaseHelper = new DatabaseHelper(this);

        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        Recipe recipe = databaseHelper.getRecipe(recipeId);

        if (recipe == null) {
            Toast.makeText(this, R.string.recipe_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        List<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(recipeId);

        textRecipeName.setText(recipe.getName());
        textIngredients.setText(buildIngredientText(ingredients));
        textMethod.setText(recipe.getSteps());
    }

    private String buildIngredientText(List<RecipeIngredient> ingredients) {
        StringBuilder builder = new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {
            builder.append("• ")
                    .append(formatQuantity(ingredient.getQuantity()))
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(ingredient.getName())
                    .append("\n");
        }

        return builder.toString().trim();
    }

    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((int) quantity);
        }

        return String.valueOf(quantity);
    }
}
