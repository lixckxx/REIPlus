package com.lix.reiplus.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "reiplus")
public class ModConfig implements ConfigData {

    // ── Search Bar — Math ─────────────────────────────────────────────────────

    public boolean enableMathCalculations    = true;
    /** Show the "= result" preview inline while typing, before pressing Enter. */
    public boolean showInlinePreview         = true;
    public boolean autoCalculateOnType       = true;
    public boolean formatNumbersWithSuffixes = true;

    /**
     * RGB colour (24-bit, NO alpha) for the " = " separator in the inline preview.
     * cloth-config2's color picker is RGB-only; alpha 0xFF is applied at render time.
     * Default: yellow (#FFFF55)
     */
    public int previewSeparatorColor = 0xFFFF55;

    /**
     * RGB colour (24-bit, NO alpha) for the result number in the inline preview.
     * cloth-config2's color picker is RGB-only; alpha 0xFF is applied at render time.
     * Default: green (#55FF55)
     */
    public int previewResultColor = 0x55FF55;

    // ── Search Bar — History ──────────────────────────────────────────────────

    /** Number of past searches kept in the Up/Down arrow history buffer. */
    public int searchHistorySize = 20;

    // ── Helper ────────────────────────────────────────────────────────────────

    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}