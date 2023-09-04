package com.mrmelon54.EnhancedSearchability.mixin.server;

import com.mrmelon54.EnhancedSearchability.EnhancedSearchability;
import com.mrmelon54.EnhancedSearchability.GuiTools;
import com.mrmelon54.EnhancedSearchability.duck.FilterSupplier;
import com.mrmelon54.EnhancedSearchability.duck.HeaderHider;
import com.mrmelon54.EnhancedSearchability.duck.ListProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(JoinMultiplayerScreen.class)
public class MixinJoinMultiplayerScreen extends Screen {
    @Shadow
    protected ServerSelectionList serverSelectionList;
    @Shadow
    private boolean initedOnce;
    @Shadow
    @Nullable
    private List<Component> toolTip;
    @Unique
    private EditBox enhanced_searchability$serverSearchBox;

    protected MixinJoinMultiplayerScreen(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void injected_init(CallbackInfo ci) {
        if (EnhancedSearchability.isServersDisabled()) return;

        Minecraft mc = Minecraft.getInstance();
        if (serverSelectionList instanceof ListProvider listDuck && serverSelectionList instanceof FilterSupplier filterDuck) {
            //noinspection Convert2MethodRef - these conversions break the mod,
            enhanced_searchability$serverSearchBox = GuiTools.addSearchBox(mc, (EditBox box) -> addWidget(box), 22, listDuck, filterDuck, enhanced_searchability$serverSearchBox);
            setInitialFocus(enhanced_searchability$serverSearchBox);
        }

        if (initedOnce && serverSelectionList instanceof HeaderHider duck)
            duck.enhanced_searchability$hideHeaderAndShift();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawCenteredString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V", shift = At.Shift.BEFORE), cancellable = true)
    private void redirect_render_title(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (EnhancedSearchability.isServersDisabled()) return;

        guiGraphics.drawCenteredString(font, title, width / 2, 8, 0xffffff);
        super.render(guiGraphics, i, j, f);
        if (enhanced_searchability$serverSearchBox != null)
            enhanced_searchability$serverSearchBox.render(guiGraphics, i, j, f);
        if (toolTip != null) guiGraphics.renderComponentTooltip(font, toolTip, i, j);
        ci.cancel();
    }
}
