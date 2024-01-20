package com.mrmelon54.EnhancedSearchability.mixin.stats;

import com.mrmelon54.EnhancedSearchability.EnhancedSearchability;
import com.mrmelon54.EnhancedSearchability.GuiTools;
import com.mrmelon54.EnhancedSearchability.duck.FilterSupplier;
import com.mrmelon54.EnhancedSearchability.duck.HeaderHider;
import com.mrmelon54.EnhancedSearchability.duck.ListProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatsScreen.class)
public abstract class MixinStatsScreen extends Screen {
    @Shadow
    private StatsScreen.GeneralStatisticsList statsList;
    @Shadow
    StatsScreen.ItemStatisticsList itemStatsList;
    @Shadow
    private StatsScreen.MobsStatisticsList mobsStatsList;

    @Shadow
    @Nullable
    private ObjectSelectionList<?> activeList;
    @Unique
    private EditBox enhanced_searchability$statSearchField;

    protected MixinStatsScreen(Component component) {
        super(component);
    }

    @Inject(method = "initLists", at = @At("TAIL"))
    private void injectedInitLists(CallbackInfo ci) {
        if (EnhancedSearchability.isStatsDisabled()) return;

        if (statsList instanceof HeaderHider duck) duck.enhanced_searchability$hideHeaderAndShift();
        if (itemStatsList instanceof HeaderHider duck) duck.enhanced_searchability$hideHeaderAndShift();
        if (mobsStatsList instanceof HeaderHider duck) duck.enhanced_searchability$hideHeaderAndShift();
    }

    @Inject(method = "initButtons", at = @At("TAIL"))
    private void injectedInitButtons(CallbackInfo ci) {
        if (EnhancedSearchability.isStatsDisabled()) return;

        Minecraft mc = Minecraft.getInstance();
        enhanced_searchability$statSearchField = GuiTools.addSearchBox(mc, this::addWidget, 22, new ListProvider() {
            @Override
            public int getRowLeft() {
                return width / 2 - 100;
            }

            @Override
            public int getRowWidth() {
                return 200;
            }
        }, searchTextSupplier -> {
            ObjectSelectionList<?> list = this.activeList;
            if (list == null) return;
            if (list instanceof FilterSupplier duck) duck.enhanced_searchability$filter(searchTextSupplier);
        }, enhanced_searchability$statSearchField);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawCenteredString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V", shift = At.Shift.BEFORE), cancellable = true)
    private void injectedRender(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (EnhancedSearchability.isStatsDisabled()) return;

        guiGraphics.drawCenteredString(font, title, width / 2, 8, 0xffffff);
        if (enhanced_searchability$statSearchField != null)
            enhanced_searchability$statSearchField.render(guiGraphics, i, j, f);
        ci.cancel();
    }
}
