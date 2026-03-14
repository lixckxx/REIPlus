package com.lix.reiplus.config;

import me.shedaniel.rei.api.client.config.addon.ConfigAddon;
import me.shedaniel.rei.api.client.config.addon.ConfigAddonRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModConfigAddon implements ConfigAddon {

    @Override
    public Component getName() {
        return Component.literal("REI Plus");
    }

    @Override
    public Component getDescription() {
        return Component.literal("Enhanced search with math calculations and search history");
    }

    @Override
    public Screen createScreen(Screen parent) {
        return ModConfigScreen.create(parent);
    }

    public static void register() {
        ConfigAddonRegistry.getInstance().register(new ModConfigAddon());
    }
}