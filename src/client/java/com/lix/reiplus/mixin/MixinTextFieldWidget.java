package com.lix.reiplus.mixin;

import com.lix.reiplus.util.SearchBarAccessor;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.impl.client.gui.widget.basewidgets.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TextFieldWidget.class, remap = false)
public abstract class MixinTextFieldWidget implements SearchBarAccessor {

    @Shadow private String text; // Matches line 62 of your source
    @Shadow private boolean focused; // Matches line 66
    @Shadow private Rectangle bounds; // Matches line 61

    @Override public String skyblock$getText() { return this.text; }
    @Override public boolean skyblock$isFocused() { return this.focused; }
    @Override public Rectangle skyblock$getBounds() { return this.bounds; }

    @Override
    public boolean skyblock$containsMouse(double x, double y) {
        // TextFieldWidget extends WidgetWithBounds which extends Widget.
        // We cast to the base Widget to call the inherited containsMouse.
        return ((Widget)(Object)this).containsMouse(x, y);
    }
}