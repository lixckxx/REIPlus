package com.lix.reiplus.mixin;

import com.lix.reiplus.util.MathUtil;
import com.lix.reiplus.util.SearchBarAccessor;
import me.shedaniel.math.Rectangle;
import me.shedaniel.math.impl.PointHelper;
import me.shedaniel.rei.impl.client.gui.widget.search.OverlaySearchField;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = OverlaySearchField.class, remap = false)
public abstract class MixinREISearchBar {

    @Inject(method = "renderBorder", at = @At("HEAD"), cancellable = true)
    private void onRenderBorder(GuiGraphics graphics, CallbackInfo ci) {
        SearchBarAccessor acc = (SearchBarAccessor) this;

        if (MathUtil.isFormula(acc.skyblock$getText())) {
            this.skyblock$drawFormulaBorder(graphics, acc);
            ci.cancel();
        }
    }

    @Unique
    private void skyblock$drawFormulaBorder(GuiGraphics graphics, SearchBarAccessor acc) {
        Rectangle b = acc.skyblock$getBounds();
        // Check hover using our bridge
        boolean isHovered = acc.skyblock$containsMouse(PointHelper.getMouseX(), PointHelper.getMouseY());

        // Use the white/gray logic from the source you provided
        int borderColor = (isHovered || acc.skyblock$isFocused()) ? 0xFFFFFFFF : 0xFFA0A0A0;

        // Exact replication of the rendering logic from your TextFieldWidget.java
        graphics.fill(b.x - 1, b.y - 1, b.x + b.width + 1, b.y + b.height + 1, 0xFF000000);
        graphics.fill(b.x, b.y, b.x + b.width, b.y + b.height, borderColor);
        graphics.fill(b.x + 1, b.y + 1, b.x + b.width - 1, b.y + b.height - 1, 0xFF000000);
    }
}