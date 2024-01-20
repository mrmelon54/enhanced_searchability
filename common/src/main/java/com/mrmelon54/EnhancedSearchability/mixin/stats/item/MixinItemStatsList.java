package com.mrmelon54.EnhancedSearchability.mixin.stats.item;

import com.mrmelon54.EnhancedSearchability.EnhancedSearchability;
import com.mrmelon54.EnhancedSearchability.GuiTools;
import com.mrmelon54.EnhancedSearchability.duck.FilterSupplier;
import com.mrmelon54.EnhancedSearchability.duck.HeaderHider;
import com.mrmelon54.EnhancedSearchability.duck.ListProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(StatsScreen.ItemStatisticsList.class)
public class MixinItemStatsList extends ObjectSelectionList<StatsScreen.ItemStatisticsList.ItemRow> implements ListProvider, FilterSupplier, HeaderHider {
    @Unique
    private final List<StatsScreen.ItemStatisticsList.ItemRow> enhanced_searchability$storeOriginal = new ArrayList<>();

    public MixinItemStatsList(Minecraft minecraft, int i, int j, int k, int l) {
        super(minecraft, i, j, k, l);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectedInit(StatsScreen statsScreen, Minecraft minecraft, CallbackInfo ci) {
        for (int i = 0; i < getItemCount(); i++) enhanced_searchability$storeOriginal.add(getEntry(i));
    }

    @Override
    public void enhanced_searchability$filter(Supplier<String> searchTextSupplier) {
        if (EnhancedSearchability.isStatsDisabled()) return;

        //noinspection Convert2MethodRef - these conversions break the mod
        GuiTools.enhanced_searchability$customAddEntriesToUI(searchTextSupplier, () -> clearEntries(), enhanced_searchability$storeOriginal, entry -> addEntry(entry));
    }

    @Override
    public void enhanced_searchability$hideHeaderAndShift() {
        setY(getY() + 15);
        setHeight(getHeight() - 15);
    }

    @Override
    public double getScrollAmount() {
        double v = super.getScrollAmount();
        int m = getMaxScroll();
        return v > m ? m : v;
    }
}
