package com.lix.reiplus.search;

import com.lix.reiplus.ReiPlusMod;
import me.shedaniel.rei.api.client.search.method.InputMethod;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MathInputMethod implements InputMethod<String> {
    private static final Logger log = LoggerFactory.getLogger(ReiPlusMod.class);

    @Override
    public Component getName() {
        return Component.literal("Math Calculator");
    }

    @Override
    public Component getDescription() {
        return Component.literal("Allows math in the search bar");
    }

    @Override
    public Iterable<String> expendFilter(String filter) {
        EnhancedSearchFilter.SearchResult result = EnhancedSearchFilter.check(filter);

        if (result != null) {
            // We found a price/calculation, return the number to REI
            return java.util.Collections.singletonList(result.getPlainValue());
        }

        return java.util.Collections.singletonList(filter);
    }

    @Override
    public List<Locale> getMatchingLocales() {
        return InputMethod.getAllLocales();
    }

    @Override
    public boolean contains(String filter, String entry) {
        return entry.toLowerCase(java.util.Locale.ROOT).contains(filter.toLowerCase(java.util.Locale.ROOT));
    }

    @Override
    public CompletableFuture<Void> prepare(Executor executor) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> dispose(Executor executor) {
        return CompletableFuture.completedFuture(null);
    }
}