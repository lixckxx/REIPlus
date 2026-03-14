package com.lix.reiplus.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import com.lix.reiplus.Calculator;

public class MathUtil {

    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###.##",
            DecimalFormatSymbols.getInstance(Locale.US));


    public static String expandSuffixes(String input) {
        if (input == null) return null;
        // Regex looks for a number followed by 'm' (case-insensitive)
        // Matches 9m, 9.5M, etc.
        return input.toLowerCase()
                .replaceAll("(\\d+(\\.\\d+)?)s", "($1*64)")
                .replaceAll("(\\d+(\\.\\d+)?)b", "($1*1000000000)")
                .replaceAll("(\\d+(\\.\\d+)?)m", "($1*1000000)")
                .replaceAll("(\\d+(\\.\\d+)?)k", "($1*1000)");
    }

    public static boolean isFormula(String input) {
        if (input == null || input.trim().isEmpty()) return false;

        // Expand "9m" to "9*1000000" so the evaluator understands it
        String processed = expandSuffixes(input);

        boolean isNumeric = processed.matches("-?\\d+(\\.\\d+)?");
        boolean hasOperator = processed.matches(".*[+\\-*/^].*");

        if (!isNumeric && !hasOperator) return false;

        return evaluate(processed) != null;
    }

    /**
     * Tries to calculate the string using the Calculator class.
     * Returns null if invalid or incomplete.
     */
    public static Double evaluate(String input) {
        try {
            // Ensure "9k" is turned into "9*1000" before calculation
            String processed = expandSuffixes(input);
            BigDecimal result = Calculator.calculate(processed);
            return result.doubleValue();
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatPlain(double number) {
        if (number == Math.floor(number) && !Double.isInfinite(number)) {
            return String.format("%d", (long) number);
        }

        return String.valueOf(number);
    }

    public static String formatNumber(double number) {
        return FORMATTER.format(number);
    }

    public static String formatWithSuggestions(double value) {
        long longVal = (long) value;

        // Check for exact matches first (optional)
        if (value % 64 == 0) {
            long stacks = longVal / 64;
            return String.format("%s (%ds)", formatNumber(value), stacks);
        }

        // Check for large numbers (Millions/Billions)
        if (value >= 1_000_000_000) {
            return String.format("%s (%.2fb)", formatNumber(value), value / 1_000_000_000.0);
        } else if (value >= 1_000_000) {
            return String.format("%s (%.2fm)", formatNumber(value), value / 1_000_000.0);
        } else if (value >= 1_000) {
            return String.format("%s (%.2fk)", formatNumber(value), value / 1_000.0);
        }

        return formatNumber(value);
    }


}