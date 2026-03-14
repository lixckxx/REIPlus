package com.lix.reiplus.util;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private static final List<String> history = new ArrayList<>();
    private static int historyIndex = 0;
    private static String currentDraft = "";

    public static void addToHistory(String equation) {
        if (equation == null || equation.trim().isEmpty()) return;

        // Don't add if it's the same as the last entry
        if (!history.isEmpty() && history.get(history.size() - 1).equals(equation)) {
            historyIndex = history.size(); // Reset index to the end anyway
            return;
        }

        history.add(equation);
        historyIndex = history.size(); // Set index to the "new" line at the end
    }

    public static String getPrevious() {
        if (history.isEmpty()) return null;

        // If we are at the end (the typing line), we stay at the last entry
        if (historyIndex > 0) {
            historyIndex--;
        }
        return history.get(historyIndex);
    }

    public static String getNext() {
        if (historyIndex < history.size() - 1) {
            historyIndex++;
            return history.get(historyIndex);
        } else {
            // If we've reached the end of history, return the draft they were typing
            historyIndex = history.size();
            return currentDraft;
        }
    }

    public static void saveDraft(String draft) {
        // We only save the draft if the user is currently at the "new line" (the end)
        if (historyIndex == history.size()) {
            currentDraft = draft;
        }
    }
}