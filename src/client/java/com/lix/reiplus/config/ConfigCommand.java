package com.lix.reiplus.config;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ConfigCommand {
    public static volatile boolean openConfigNextTick = false;

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommandManager.literal("reiplus")
                        .executes(ctx -> {
                            openConfigNextTick = true;
                            return 1;
                        })
        );
    }
}