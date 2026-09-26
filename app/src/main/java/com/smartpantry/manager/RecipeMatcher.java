package com.smartpantry.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeMatcher {

    private RecipeMatcher() {
        // Utility class
    }

    public static boolean canMakeRecipe(
            List<PantryItem> pantryItems,
            List<RecipeIngredient> requiredIngredients) {

        if (pantryItems == null || pantryItems.isEmpty()
                || requiredIngredients == null || requiredIngredients.isEmpty()) {
            return false;
        }

        Map<String, Double> requiredTotals = new HashMap<>();

        for (RecipeIngredient ingredient : requiredIngredients) {
            if (ingredient == null) {
                return false;
            }

            String name = IngredientUtils.normalizeIngredientName(ingredient.getName());
            String unit = IngredientUtils.normalizeUnit(ingredient.getUnit());
            double quantity = IngredientUtils.convertToBaseQuantity(
                    ingredient.getQuantity(), ingredient.getUnit());

            if (name.isEmpty() || unit.isEmpty() || !isValidQuantity(quantity)) {
                return false;
            }

            String key = name + "\u0000" + unit;
            double total = requiredTotals.getOrDefault(key, 0.0) + quantity;

            if (!isValidQuantity(total)) {
                return false;
            }

            requiredTotals.put(key, total);
        }

        Map<String, Double> availableTotals = new HashMap<>();

        for (PantryItem item : pantryItems) {
            if (item == null) {
                continue;
            }

            String name = IngredientUtils.normalizeIngredientName(item.getName());
            String unit = IngredientUtils.normalizeUnit(item.getUnit());
            double quantity = IngredientUtils.convertToBaseQuantity(
                    item.getQuantity(), item.getUnit());

            if (name.isEmpty() || unit.isEmpty() || !isValidQuantity(quantity)) {
                continue;
            }

            String key = name + "\u0000" + unit;

            if (requiredTotals.containsKey(key)) {
                double total = availableTotals.getOrDefault(key, 0.0) + quantity;

                if (!isValidQuantity(total)) {
                    return false;
                }

                availableTotals.put(key, total);
            }
        }

        for (Map.Entry<String, Double> requirement : requiredTotals.entrySet()) {
            double available = availableTotals.getOrDefault(requirement.getKey(), 0.0);

            if (available < requirement.getValue()) {
                return false;
            }
        }

        return true;
    }

    private static boolean isValidQuantity(double quantity) {
        return quantity > 0 && !Double.isNaN(quantity) && !Double.isInfinite(quantity);
    }
}
