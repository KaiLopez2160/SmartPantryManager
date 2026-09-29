# Smart Pantry Manager

Smart Pantry Manager is a Java Android application for tracking ingredients at home and suggesting recipes that can be made using **only** the ingredients and quantities already in the pantry.

## Features

- Add, view, edit and delete pantry ingredients (name, quantity and unit).
- Save pantry items locally so they remain after the app closes.
- Seed 18 South African inspired recipes in the local database.
- Show only recipes for which every required ingredient is present in enough quantity.
- Open a recipe to see its full ingredient list and preparation steps.
- Use the Pantry, Recipes and Settings navigation buttons.
- Choose whether pantry quantities are displayed in compact units (for example, 1500 g as 1.5 kg).
- Show an explanatory message when the pantry is empty or no recipe matches.

## Database

The app uses **SQLite with `SQLiteOpenHelper`**, stored on the device. This was chosen because it is simple to use in a small Java Android app, supports CRUD without an account or server and works offline. `DatabaseHelper.java` manages three tables: `pantry_items`, `recipes` and `recipe_ingredients`. The 18 recipes are inserted when the database is first created. An upgrade from database version 1 to 2 adds the recipe tables without deleting pantry data.

## Strict recipe matching

`DatabaseHelper.getSuggestedRecipes()` checks each seeded recipe with `RecipeMatcher.canMakeRecipe()`. A recipe qualifies only when **every** required ingredient is available in at least its required quantity. Duplicate pantry entries are added together and duplicate recipe requirements are added together so the same stock cannot be counted twice. `IngredientUtils` handles basic name variants such as `tomatoes`/`tomato`, and compatible quantities such as `1 kg`/`1000 g` and `1 L`/`1000 ml`. Incompatible units, such as items and grams, are not guessed or converted. The compact units setting changes the display only, not saved quantities or matching.

## Requirements

- Android Studio with Android SDK Platform 35 installed.
- JDK 17 for the Android build.
- Gradle 8.9 for the project's Android Gradle Plugin 8.7.3.
- An Android emulator or Android device running Android 7.0 (API 24) or later.
- Internet access for the initial Gradle dependency download. The app itself runs offline.

## Set up and run

1. Clone this repository: `git clone https://github.com/KaiLopez2160/SmartPantryManager.git`.
2. Open the `SmartPantryManager` folder in Android Studio (the folder containing `settings.gradle`).
3. Use JDK 17 for Gradle, install Android SDK Platform 35 if Android Studio asks, and allow Gradle to sync and download dependencies.
4. This repositorys initial Android project did not include Gradle wrapper files. If Android Studio requests a Gradle distribution, use Gradle 8.9. Alternatively, with Gradle 8.9 installed, run `gradle wrapper --gradle-version 8.9` once in the project folder, then reopen/sync the project using the generated wrapper.
5. Start an Android emulator or connect a supported Android device, select the `app` run configuration and press Run.

## Demonstrating the strict rule

For **Egg and Tomato Breakfast**, add these pantry ingredients: `Egg` (2 items), `Tomato` (1 item), `Oil` (10 ml) and `Salt` (2 g). The recipe should appear under Suggested Recipes. Remove or reduce one required ingredient below its required quantity and the recipe should disappear. Add enough of that ingredient again and it should reappear. To demonstrate persistence, close and reopen the app and verify that the saved pantry items remain.
