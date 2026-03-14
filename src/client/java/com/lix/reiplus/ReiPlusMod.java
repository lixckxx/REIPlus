package com.lix.reiplus;

import com.lix.reiplus.config.ConfigCommand;
import com.lix.reiplus.config.ModConfig;
import com.lix.reiplus.config.ModConfigAddon;
import com.lix.reiplus.config.ModConfigScreen;
import com.lix.reiplus.search.EnhancedSearchFilter;
import com.lix.reiplus.util.HistoryManager;
import com.lix.reiplus.util.MathUtil;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.rei.api.client.gui.screen.DisplayScreen;
import me.shedaniel.rei.impl.client.gui.widget.basewidgets.TextFieldWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReiPlusMod implements ClientModInitializer {
    private static final Logger log = LoggerFactory.getLogger(ReiPlusMod.class);

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        ModConfigAddon.register();
        EnhancedSearchFilter.init();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                ConfigCommand.register(dispatcher));

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (ConfigCommand.openConfigNextTick) {
                ConfigCommand.openConfigNextTick = false;
                client.setScreen(ModConfigScreen.create(null));
            }
        });

        ScreenEvents.BEFORE_INIT.register((client, screen, sw, sh) -> {
            if (screen instanceof AbstractContainerScreen<?> || screen instanceof DisplayScreen) {
                ScreenKeyboardEvents.allowKeyPress(screen).register((scr, key) -> {
                    var searchField = me.shedaniel.rei.api.client.REIRuntime.getInstance().getSearchTextField();
                    if (searchField == null) return true;

                    if (key.key() == 265) {
                        HistoryManager.saveDraft(searchField.getText());
                        String prev = HistoryManager.getPrevious();
                        if (prev != null) {
                            searchField.setText(prev);
                            return false;
                        }
                    } else if (key.key() == 264) {
                        String next = HistoryManager.getNext();
                        if (next != null) {
                            searchField.setText(next);
                            return false;
                        }
                    } else if (key.key() == 257 || key.key() == 335) {
                        String currentText = searchField.getText();
                        EnhancedSearchFilter.SearchResult result = EnhancedSearchFilter.check(currentText);

                        if (result != null) {
                            HistoryManager.addToHistory(currentText);
                            searchField.setText(result.getPlainValue()); // Sets bar to "500000"
                            return false;
                        }
                    }
                    return true;
                });
            }
        });

        ScreenEvents.AFTER_INIT.register((client, screen, sw, sh) -> {
            if (screen instanceof DisplayScreen || screen instanceof AbstractContainerScreen<?>) {
                var searchField = me.shedaniel.rei.api.client.REIRuntime.getInstance().getSearchTextField();
                if (!(searchField instanceof TextFieldWidget widget)) return;

                widget.setFormatter((textField, text, index) -> {
                    if (MathUtil.isFormula( textField.getText())) {
                        return net.minecraft.util.FormattedCharSequence.forward(
                                text, net.minecraft.network.chat.Style.EMPTY.withColor(0xFFFFFFFF));
                    }
                    return net.minecraft.util.FormattedCharSequence.forward(
                            text, net.minecraft.network.chat.Style.EMPTY);
                });

                ScreenEvents.afterRender(screen).register((scr, guiGraphics, mouseX, mouseY, tickDelta) -> {
                    ModConfig cfg = ModConfig.get();
                    if (!cfg.showInlinePreview) return;

                    String fullText = widget.getText();
                    EnhancedSearchFilter.SearchResult result = EnhancedSearchFilter.check(fullText);

                    if (result == null) return;

                    var bounds    = widget.getBounds();
                    int textWidth = client.font.width(fullText);
                    int x         = bounds.getX() + 4 + textWidth;
                    int y         = bounds.getY() + (bounds.getHeight() - 8) / 2;

                    // Config stores 24-bit RGB (no alpha) because cloth-config's picker is RGB-only.
                    // OR with 0xFF000000 here to produce the full ARGB value Minecraft's renderer expects.
                    int separatorColor = 0xFF000000 | cfg.previewSeparatorColor;
                    int resultColor    = 0xFF000000 | cfg.previewResultColor;

                    var preview = net.minecraft.network.chat.Component.empty()
                            .append(net.minecraft.network.chat.Component.literal(" = ")
                                    .withStyle(s -> s.withColor(separatorColor)))
                            .append(net.minecraft.network.chat.Component.literal(MathUtil.formatNumber(result.value()))
                                    .withStyle(s -> s.withColor(resultColor)));


                    guiGraphics.drawString(client.font, preview.getVisualOrderText(), x, y, -1, false);
                });
            }
        });
    }
}