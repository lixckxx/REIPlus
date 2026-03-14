package com.lix.reiplus.search;

import com.lix.reiplus.config.ModConfig;
import com.lix.reiplus.util.MathUtil;
import org.jetbrains.annotations.Nullable;

public class EnhancedSearchFilter {

    // Data holder to separate the raw number from the display text
    public record SearchResult(double value, String displaySuffix) {
        public String getFullDisplay(String original) {
            return original + " = " + MathUtil.formatNumber(value) + (displaySuffix.isEmpty() ? "" : " (" + displaySuffix + ")");
        }
        public String getPlainValue() {
            return MathUtil.formatPlain(value);
        }
    }

    public static void init() {}

    @Nullable
    public static SearchResult check(String searchText) {
        if (searchText == null || searchText.isEmpty()) return null;

        ModConfig config = ModConfig.get();

        if (config.enableMathCalculations && MathUtil.isFormula(searchText)) {
            Double val = MathUtil.evaluate(searchText);
            if (val != null) return new SearchResult(val, "");
        }

        return null;
    }
}