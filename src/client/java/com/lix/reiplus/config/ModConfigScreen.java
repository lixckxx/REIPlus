package com.lix.reiplus.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * REI Skyblock configuration screen.
 *
 * NOTE ON SubCategoryBuilder.add():
 *   In several cloth-config2 builds the method returns boolean (Collection contract),
 *   NOT SubCategoryBuilder.  Chaining  .add().add()  therefore fails with
 *   "boolean cannot be dereferenced".
 *   Fix: hold the builder in a local variable and call .add() as separate statements.
 */
public class ModConfigScreen {

    public static Screen create(Screen parent) {
        ModConfig config = ModConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("REI+"))
                .setSavingRunnable(() -> AutoConfig.getConfigHolder(ModConfig.class).save());

        ConfigEntryBuilder e = builder.entryBuilder();

        // ══════════════════════════════════════════════════════════════════════
        // CATEGORY 1 — Search Bar
        //   Math sub-section (master toggle + children) → Price query → History
        // ══════════════════════════════════════════════════════════════════════

        ConfigCategory searchBar = builder.getOrCreateCategory(Component.literal("Search Bar"));

        // ── Math Calculations ─────────────────────────────────────────────────
        // SubCategoryBuilder.add() may return boolean in older cloth-config2 builds.
        // Do NOT chain calls — use the builder reference on separate lines.

        SubCategoryBuilder mathBuilder = e.startSubCategory(
                Component.literal("Math Calculations"));

        mathBuilder.add(e.startBooleanToggle(
                        Component.literal("Enable Math Calculations"),
                        config.enableMathCalculations)
                .setDefaultValue(true)
                .setTooltip(Component.literal(
                        "Evaluate math expressions directly in the REI search bar.\n"
                                + "Example: type  64 * 12  to see 768."))
                .setSaveConsumer(val -> config.enableMathCalculations = val)
                .build());

        // Child of "Enable Math Calculations" — only useful when math is on.
        mathBuilder.add(e.startBooleanToggle(
                        Component.literal("Auto Calculate While Typing"),
                        config.autoCalculateOnType)
                .setDefaultValue(true)
                .setTooltip(Component.literal(
                        "Re-evaluate the expression on every keystroke.\n"
                                + "Disable to only evaluate when you press Enter."))
                .setSaveConsumer(val -> config.autoCalculateOnType = val)
                .build());

        // ── Inline Preview sub-section ────────────────────────────────────────
        // Nested inside Math because it only renders when math is active.
        // Colour pickers are child settings of the preview toggle.

        SubCategoryBuilder previewBuilder = e.startSubCategory(
                Component.literal("Inline Preview  (= result)"));

        previewBuilder.add(e.startBooleanToggle(
                        Component.literal("Show Inline Preview"),
                        config.showInlinePreview)
                .setDefaultValue(true)
                .setTooltip(Component.literal(
                        "Display  = result  next to the cursor while you type a formula.\n"
                                + "Press Enter to commit the result into the search field."))
                .setSaveConsumer(val -> config.showInlinePreview = val)
                .build());

        // cloth-config2's color picker is 24-bit RGB only — no alpha channel.
        // We mask to 0xFFFFFF on both read (passed to picker) and write (saved to field).
        // Alpha 0xFF is applied at render time in ExampleModClient, not stored here.
        previewBuilder.add(e.startColorField(
                        Component.literal("Separator Colour  \" = \""),
                        config.previewSeparatorColor & 0xFFFFFF)
                .setDefaultValue(0xFFFF55)
                .setTooltip(Component.literal(
                        "Colour of the  \" = \"  label in the inline preview.\n"
                                + "Default: yellow  (#FFFF55)"))
                .setSaveConsumer(val -> config.previewSeparatorColor = val & 0xFFFFFF)
                .build());

        previewBuilder.add(e.startColorField(
                        Component.literal("Result Number Colour"),
                        config.previewResultColor & 0xFFFFFF)
                .setDefaultValue(0x55FF55)
                .setTooltip(Component.literal(
                        "Colour of the result number in the inline preview.\n"
                                + "Default: green  (#55FF55)"))
                .setSaveConsumer(val -> config.previewResultColor = val & 0xFFFFFF)
                .build());

        previewBuilder.setExpanded(true);
        mathBuilder.add(previewBuilder.build());

        mathBuilder.setExpanded(true);
        searchBar.addEntry(mathBuilder.build());

        // ── Number Formatting — affects both math and price output ────────────

        searchBar.addEntry(e.startBooleanToggle(
                        Component.literal("Format Numbers  (1k / 1m / 1b)"),
                        config.formatNumbersWithSuffixes)
                .setDefaultValue(true)
                .setTooltip(Component.literal(
                        "Display large numbers with short suffixes instead of full digits.\n"
                                ))
                .setSaveConsumer(val -> config.formatNumbersWithSuffixes = val)
                .build());

        // ── Search History ────────────────────────────────────────────────────

        SubCategoryBuilder historyBuilder = e.startSubCategory(
                Component.literal("Search History"));

        historyBuilder.add(e.startIntSlider(
                        Component.literal("History Buffer Size"),
                        config.searchHistorySize, 5, 100)
                .setDefaultValue(20)
                .setTooltip(Component.literal(
                        "How many past searches are kept in the Up/Down arrow history.\n"
                                + "Lower values use less memory; higher values let you reach older queries."))
                .setSaveConsumer(val -> config.searchHistorySize = val)
                .build());

        historyBuilder.setExpanded(true);
        searchBar.addEntry(historyBuilder.build());




        return builder.build();
    }
}