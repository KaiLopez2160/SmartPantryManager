package com.smartpantry.manager;

import java.util.List;

public class RecipeMatcher {

    private static final double QUANTITY_TOLERANCE = 0.000001;

    private RecipeMatcher() {
        // Utility class
    }

    public static boolean canMakeRecipe(
            List<PantryItem> pantryItems,
            List<RecipeIngredient> requiredIngredients) {

        if (pantryItems == null || pantryItems.isEmpty()) {
            return false;
        }

        if (requiredIngredients == null || requiredIngredients.isEmpty()) {
            return false;
        }

        for (RecipeIngredient requiredIngredient : requiredIngredients) {
            double availableQuantity = getAvailableQuantity(
                    pantryItems,
                    requiredIngredient
            );

            double requiredQuantity = IngredientUtils.convertToBaseQuantity(
                    requiredIngredient.getQuantity(),
                    requiredIngredient.getUnit()
            );

            if (availableQuantity + QUANTITY_TOLERANCE < requiredQuantity) {
                return false;
            }
        }

        return true;
    }

    private static double getAvailableQuantity(
            List<PantryItem> pantryItems,
            RecipeIngredient requiredIngredient) {

        String requiredName = IngredientUtils.normalizeIngredientName(
                requiredIngredient.getName()
        );

        double totalQuantity = 0;

        for (PantryItem pantryItem : pantryItems) {
            String pantryName = IngredientUtils.normalizeIngredientName(
                    pantryItem.getName()
            );

            boolean sameIngredient = requiredName.equals(pantryName);
            boolean compatibleUnits = IngredientUtils.unitsAreCompatible(
                    pantryItem.getUnit(),
                    requiredIngredient.getUnit()
            );

            if (sameIngredient && compatibleUnits) {
                totalQuantity += IngredientUtils.convertToBaseQuantity(
                        pantryItem.getQuantity(),
                        pantryItem.getUnit()
                );
            }
        }

        return totalQuantity;
    }
}
