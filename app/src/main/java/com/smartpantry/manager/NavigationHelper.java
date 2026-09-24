package com.smartpantry.manager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NavigationHelper {

    private NavigationHelper() {
    }

    public static void setup(AppCompatActivity activity, int selectedTab) {
        Button pantry = activity.findViewById(R.id.navPantry);
        Button recipes = activity.findViewById(R.id.navRecipes);
        Button settings = activity.findViewById(R.id.navSettings);

        highlight(pantry, selectedTab == R.id.navPantry, activity);
        highlight(recipes, selectedTab == R.id.navRecipes, activity);
        highlight(settings, selectedTab == R.id.navSettings, activity);

        pantry.setOnClickListener(view -> open(activity, PantryActivity.class));
        recipes.setOnClickListener(view -> open(activity, SuggestedRecipesActivity.class));
        settings.setOnClickListener(view -> open(activity, SettingsActivity.class));
    }

    private static void highlight(Button button, boolean selected,
                                  AppCompatActivity activity) {
        int background = selected ? R.color.pink : R.color.card_navy;
        int foreground = selected ? R.color.navy : R.color.off_white;
        button.setBackgroundTintList(ColorStateList.valueOf(activity.getColor(background)));
        button.setTextColor(activity.getColor(foreground));
    }

    private static void open(AppCompatActivity activity, Class<?> destination) {
        if (activity.getClass() == destination) {
            return;
        }

        Intent intent = new Intent(activity, destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }
}
