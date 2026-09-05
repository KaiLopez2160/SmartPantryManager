package com.smartpantry.manager;

import java.util.Locale;

public class IngredientUtils {

    public static String normalizeIngredientName(String name) {
        if (name == null) {
            return "";
        }

        String normalized = name
                .trim()
                .toLowerCase(Locale.ROOT)
                .replace("-", " ")
                .replaceAll("\\s+", " ");

        String[] words = normalized.split(" ");

        if (words.length > 0) {
            int lastIndex = words.length - 1;
            words[lastIndex] = makeSingular(words[lastIndex]);
        }

        return String.join(" ", words);
    }

    private static String makeSingular(String word) {
        if (word.endsWith("oes") && word.length() > 3) {
            return word.substring(0, word.length() - 2);
        }

        if (word.endsWith("ies") && word.length() > 3) {
            return word.substring(0, word.length() - 3) + "y";
        }

        if (word.endsWith("s")
                && !word.endsWith("ss")
                && word.length() > 1) {
            return word.substring(0, word.length() - 1);
        }

        return word;
    }

    public static String normalizeUnit(String unit) {
        if (unit == null) {
            return "";
        }

        String normalized = unit.trim().toLowerCase(Locale.ROOT);

        switch (normalized) {
            case "g":
            case "gram":
            case "grams":
            case "kg":
            case "kgs":
            case "kilogram":
            case "kilograms":
                return "g";

            case "ml":
            case "millilitre":
            case "millilitres":
            case "milliliter":
            case "milliliters":
            case "l":
            case "litre":
            case "litres":
            case "liter":
            case "liters":
                return "ml";

            case "item":
            case "items":
            case "piece":
            case "pieces":
            case "each":
            case "unit":
            case "units":
                return "item";

            case "slice":
            case "slices":
                return "slice";

            default:
                return normalized;
        }
    }

    public static double convertToBaseQuantity(double quantity, String unit) {
        if (unit == null) {
            return quantity;
        }

        String normalized = unit.trim().toLowerCase(Locale.ROOT);

        if (normalized.equals("kg")
                || normalized.equals("kgs")
                || normalized.equals("kilogram")
                || normalized.equals("kilograms")) {
            return quantity * 1000;
        }

        if (normalized.equals("l")
                || normalized.equals("litre")
                || normalized.equals("litres")
                || normalized.equals("liter")
                || normalized.equals("liters")) {
            return quantity * 1000;
        }

        return quantity;
    }

    public static boolean unitsAreCompatible(String firstUnit, String secondUnit) {
        return normalizeUnit(firstUnit).equals(normalizeUnit(secondUnit));
    }
}
