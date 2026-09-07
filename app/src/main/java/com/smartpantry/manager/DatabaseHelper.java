package com.smartpantry.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "ingredient_name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";

    public static final String TABLE_RECIPES = "recipes";
    public static final String RECIPE_COLUMN_ID = "id";
    public static final String RECIPE_COLUMN_NAME = "recipe_name";
    public static final String RECIPE_COLUMN_STEPS = "steps";

    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String INGREDIENT_COLUMN_ID = "id";
    public static final String INGREDIENT_COLUMN_RECIPE_ID = "recipe_id";
    public static final String INGREDIENT_COLUMN_NAME = "ingredient_name";
    public static final String INGREDIENT_COLUMN_QUANTITY = "quantity";
    public static final String INGREDIENT_COLUMN_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createPantryTable(db);
        createRecipeTables(db);
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            createRecipeTables(db);
            seedRecipes(db);
        }
    }

    private void createPantryTable(SQLiteDatabase db) {
        String createPantryTable = "CREATE TABLE IF NOT EXISTS " + TABLE_PANTRY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_QUANTITY + " REAL NOT NULL, " +
                COLUMN_UNIT + " TEXT NOT NULL)";

        db.execSQL(createPantryTable);
    }

    private void createRecipeTables(SQLiteDatabase db) {
        String createRecipeTable = "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + " (" +
                RECIPE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RECIPE_COLUMN_NAME + " TEXT NOT NULL UNIQUE, " +
                RECIPE_COLUMN_STEPS + " TEXT NOT NULL)";

        String createRecipeIngredientTable = "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPE_INGREDIENTS + " (" +
                INGREDIENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                INGREDIENT_COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                INGREDIENT_COLUMN_NAME + " TEXT NOT NULL, " +
                INGREDIENT_COLUMN_QUANTITY + " REAL NOT NULL, " +
                INGREDIENT_COLUMN_UNIT + " TEXT NOT NULL)";

        db.execSQL(createRecipeTable);
        db.execSQL(createRecipeIngredientTable);
    }

    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());

        return db.insert(TABLE_PANTRY, null, values);
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> pantryItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT));

                pantryItems.add(new PantryItem(id, name, quantity, unit));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return pantryItems;
    }

    public PantryItem getPantryItem(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        PantryItem item = null;

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
            String unit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT));

            item = new PantryItem(id, name, quantity, unit);
        }

        cursor.close();
        return item;
    }

    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());

        return db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(item.getId())}
        );
    }

    public int deletePantryItem(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                RECIPE_COLUMN_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPE_COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_COLUMN_NAME));
                String steps = cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_COLUMN_STEPS));

                recipes.add(new Recipe(id, name, steps));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return recipes;
    }

    public Recipe getRecipe(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                RECIPE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        Recipe recipe = null;

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_COLUMN_NAME));
            String steps = cursor.getString(cursor.getColumnIndexOrThrow(RECIPE_COLUMN_STEPS));
            recipe = new Recipe(id, name, steps);
        }

        cursor.close();
        return recipe;
    }

    public List<RecipeIngredient> getRecipeIngredients(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                INGREDIENT_COLUMN_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                INGREDIENT_COLUMN_ID + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(INGREDIENT_COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(INGREDIENT_COLUMN_NAME));
                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(INGREDIENT_COLUMN_QUANTITY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(INGREDIENT_COLUMN_UNIT));

                ingredients.add(new RecipeIngredient(id, recipeId, name, quantity, unit));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return ingredients;
    }

    public List<Recipe> getSuggestedRecipes() {
        List<Recipe> suggestedRecipes = new ArrayList<>();
        List<PantryItem> pantryItems = getAllPantryItems();
        List<Recipe> recipes = getAllRecipes();

        for (Recipe recipe : recipes) {
            List<RecipeIngredient> requiredIngredients =
                    getRecipeIngredients(recipe.getId());

            if (RecipeMatcher.canMakeRecipe(pantryItems, requiredIngredients)) {
                suggestedRecipes.add(recipe);
            }
        }

        return suggestedRecipes;
    }

    private void seedRecipes(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_RECIPES, null);
        int recipeCount = 0;

        if (cursor.moveToFirst()) {
            recipeCount = cursor.getInt(0);
        }
        cursor.close();

        if (recipeCount > 0) {
            return;
        }

        addSeedRecipe(db,
                "Pap and Tomato Relish",
                "1. Cook the maize meal with the water and salt until thick.\n" +
                        "2. Fry the onion in oil until soft.\n" +
                        "3. Add the tomatoes and cook into a simple relish.\n" +
                        "4. Serve the tomato relish with the pap.",
                new String[][]{
                        {"Maize meal", "200", "g"},
                        {"Water", "500", "ml"},
                        {"Tomato", "2", "item"},
                        {"Onion", "1", "item"},
                        {"Oil", "15", "ml"},
                        {"Salt", "5", "g"}
                });

        addSeedRecipe(db,
                "Chakalaka",
                "1. Fry the onion in oil until soft.\n" +
                        "2. Add the curry powder and carrots.\n" +
                        "3. Add the tomatoes and cook until softened.\n" +
                        "4. Stir in the baked beans and heat through.",
                new String[][]{
                        {"Onion", "1", "item"},
                        {"Tomato", "2", "item"},
                        {"Carrot", "2", "item"},
                        {"Baked beans", "400", "g"},
                        {"Curry powder", "5", "g"},
                        {"Oil", "15", "ml"}
                });

        addSeedRecipe(db,
                "Boerewors and Pap",
                "1. Cook the maize meal with the water and salt until thick.\n" +
                        "2. Fry or grill the boerewors until cooked through.\n" +
                        "3. Serve the boerewors with the pap.",
                new String[][]{
                        {"Boerewors", "400", "g"},
                        {"Maize meal", "200", "g"},
                        {"Water", "500", "ml"},
                        {"Salt", "5", "g"}
                });

        addSeedRecipe(db,
                "Bobotie",
                "1. Soak the bread in some of the milk.\n" +
                        "2. Fry the onion and minced beef with the curry powder.\n" +
                        "3. Mix in the soaked bread and place in a baking dish.\n" +
                        "4. Beat the eggs with the remaining milk, pour over the mince and bake until set.",
                new String[][]{
                        {"Minced beef", "500", "g"},
                        {"Onion", "1", "item"},
                        {"Bread", "2", "slice"},
                        {"Milk", "250", "ml"},
                        {"Egg", "2", "item"},
                        {"Curry powder", "10", "g"}
                });

        addSeedRecipe(db,
                "Tomato Bredie",
                "1. Brown the beef in the oil.\n" +
                        "2. Add the onion and cook until soft.\n" +
                        "3. Add the tomatoes, potatoes and salt.\n" +
                        "4. Simmer gently until the beef and potatoes are tender.",
                new String[][]{
                        {"Beef", "500", "g"},
                        {"Tomato", "4", "item"},
                        {"Onion", "1", "item"},
                        {"Potato", "2", "item"},
                        {"Oil", "15", "ml"},
                        {"Salt", "5", "g"}
                });

        addSeedRecipe(db,
                "Chicken Curry",
                "1. Fry the onion in oil until soft.\n" +
                        "2. Stir in the curry powder.\n" +
                        "3. Add the chicken, tomatoes and potatoes.\n" +
                        "4. Simmer until the chicken and potatoes are cooked through.",
                new String[][]{
                        {"Chicken", "500", "g"},
                        {"Onion", "1", "item"},
                        {"Tomato", "2", "item"},
                        {"Potato", "2", "item"},
                        {"Curry powder", "15", "g"},
                        {"Oil", "15", "ml"}
                });

        addSeedRecipe(db,
                "Samp and Beans",
                "1. Soak the samp and beans if needed.\n" +
                        "2. Add them to a pot with the water.\n" +
                        "3. Simmer until soft, adding more cooking time if required.\n" +
                        "4. Season with salt before serving.",
                new String[][]{
                        {"Samp", "250", "g"},
                        {"Dry beans", "200", "g"},
                        {"Water", "1000", "ml"},
                        {"Salt", "5", "g"}
                });

        addSeedRecipe(db,
                "Vetkoek",
                "1. Mix the flour, yeast, sugar and salt.\n" +
                        "2. Add the water and knead into a soft dough.\n" +
                        "3. Leave the dough to rise until doubled in size.\n" +
                        "4. Shape portions and fry in hot oil until golden.",
                new String[][]{
                        {"Flour", "500", "g"},
                        {"Yeast", "10", "g"},
                        {"Sugar", "10", "g"},
                        {"Salt", "5", "g"},
                        {"Water", "300", "ml"},
                        {"Oil", "500", "ml"}
                });

        addSeedRecipe(db,
                "Curried Mince",
                "1. Fry the onion in the oil.\n" +
                        "2. Add the minced beef and cook until browned.\n" +
                        "3. Stir in the curry powder and tomatoes.\n" +
                        "4. Season with salt and simmer until cooked through.",
                new String[][]{
                        {"Minced beef", "500", "g"},
                        {"Onion", "1", "item"},
                        {"Tomato", "2", "item"},
                        {"Curry powder", "10", "g"},
                        {"Oil", "15", "ml"},
                        {"Salt", "5", "g"}
                });

        addSeedRecipe(db,
                "Cheese and Tomato Toast",
                "1. Butter the bread.\n" +
                        "2. Add the sliced tomato and cheese.\n" +
                        "3. Toast until the bread is crisp and the cheese has melted.",
                new String[][]{
                        {"Bread", "2", "slice"},
                        {"Cheese", "50", "g"},
                        {"Tomato", "1", "item"},
                        {"Butter", "10", "g"}
                });

        addSeedRecipe(db,
                "Egg and Tomato Breakfast",
                "1. Heat the oil in a pan.\n" +
                        "2. Cook the chopped tomato until slightly soft.\n" +
                        "3. Add the eggs and cook to your preference.\n" +
                        "4. Season with salt.",
                new String[][]{
                        {"Egg", "2", "item"},
                        {"Tomato", "1", "item"},
                        {"Oil", "10", "ml"},
                        {"Salt", "2", "g"}
                });

        addSeedRecipe(db,
                "Potato Fritters",
                "1. Grate the potatoes.\n" +
                        "2. Mix the potato with the egg, flour and salt.\n" +
                        "3. Heat the oil in a pan.\n" +
                        "4. Fry spoonfuls of the mixture until golden on both sides.",
                new String[][]{
                        {"Potato", "3", "item"},
                        {"Egg", "1", "item"},
                        {"Flour", "50", "g"},
                        {"Oil", "50", "ml"},
                        {"Salt", "3", "g"}
                });

        addSeedRecipe(db,
                "Fried Egg and Pap",
                "1. Cook the maize meal with the water and salt until thick.\n" +
                        "2. Heat the oil in a pan.\n" +
                        "3. Fry the eggs to your preference.\n" +
                        "4. Serve the eggs with the pap.",
                new String[][]{
                        {"Egg", "2", "item"},
                        {"Maize meal", "150", "g"},
                        {"Water", "400", "ml"},
                        {"Oil", "10", "ml"},
                        {"Salt", "3", "g"}
                });

        addSeedRecipe(db,
                "Pumpkin Mash",
                "1. Cook the pumpkin until soft.\n" +
                        "2. Drain and mash it.\n" +
                        "3. Mix in the butter, sugar and cinnamon.\n" +
                        "4. Serve warm.",
                new String[][]{
                        {"Pumpkin", "500", "g"},
                        {"Butter", "20", "g"},
                        {"Sugar", "10", "g"},
                        {"Cinnamon", "2", "g"}
                });

        addSeedRecipe(db,
                "Bean Curry",
                "1. Fry the onion in the oil.\n" +
                        "2. Add the curry powder and tomatoes.\n" +
                        "3. Stir in the canned beans.\n" +
                        "4. Simmer until hot and slightly thickened.",
                new String[][]{
                        {"Canned beans", "400", "g"},
                        {"Tomato", "2", "item"},
                        {"Onion", "1", "item"},
                        {"Curry powder", "10", "g"},
                        {"Oil", "15", "ml"}
                });

        addSeedRecipe(db,
                "Tuna Mayo Sandwich",
                "1. Drain the tuna.\n" +
                        "2. Mix the tuna with the mayonnaise.\n" +
                        "3. Divide the mixture between the bread slices and make sandwiches.",
                new String[][]{
                        {"Bread", "4", "slice"},
                        {"Canned tuna", "170", "g"},
                        {"Mayonnaise", "30", "g"}
                });

        addSeedRecipe(db,
                "Chicken Bredie",
                "1. Brown the chicken in the oil.\n" +
                        "2. Add the onion and cook until soft.\n" +
                        "3. Add the tomatoes, potatoes and carrots.\n" +
                        "4. Simmer until the chicken and vegetables are tender.",
                new String[][]{
                        {"Chicken", "500", "g"},
                        {"Potato", "2", "item"},
                        {"Onion", "1", "item"},
                        {"Tomato", "2", "item"},
                        {"Carrot", "2", "item"},
                        {"Oil", "15", "ml"}
                });

        addSeedRecipe(db,
                "Crustless Milk Tart",
                "1. Warm the milk gently.\n" +
                        "2. Whisk the eggs, sugar and flour together.\n" +
                        "3. Slowly mix in the warm milk and butter.\n" +
                        "4. Cook or bake until set, then sprinkle with cinnamon.",
                new String[][]{
                        {"Milk", "500", "ml"},
                        {"Flour", "50", "g"},
                        {"Sugar", "100", "g"},
                        {"Egg", "2", "item"},
                        {"Butter", "30", "g"},
                        {"Cinnamon", "2", "g"}
                });
    }

    private void addSeedRecipe(SQLiteDatabase db, String name, String steps, String[][] ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(RECIPE_COLUMN_NAME, name);
        recipeValues.put(RECIPE_COLUMN_STEPS, steps);

        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        if (recipeId == -1) {
            return;
        }

        for (String[] ingredient : ingredients) {
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put(INGREDIENT_COLUMN_RECIPE_ID, recipeId);
            ingredientValues.put(INGREDIENT_COLUMN_NAME, ingredient[0]);
            ingredientValues.put(INGREDIENT_COLUMN_QUANTITY, Double.parseDouble(ingredient[1]));
            ingredientValues.put(INGREDIENT_COLUMN_UNIT, ingredient[2]);

            db.insert(TABLE_RECIPE_INGREDIENTS, null, ingredientValues);
        }
    }
}
