package com.mrmelon54.EnhancedSearchability.mixin.stats.item;

import com.mrmelon54.EnhancedSearchability.duck.StatsEntryDuck;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StatsScreen.ItemStatisticsList.ItemRow.class)
public class ItemRowMixin implements StatsEntryDuck {
    @Shadow
    @Final
    private Item item;

    @Override
    public Component enhanced_searchability$getText() {
        return item.getDescription();
    }
}
