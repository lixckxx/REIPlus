package com.lix.reiplus.util;

import me.shedaniel.math.Rectangle;

public interface SearchBarAccessor {
    String skyblock$getText();
    boolean skyblock$isFocused();
    Rectangle skyblock$getBounds();
    boolean skyblock$containsMouse(double x, double y);
}