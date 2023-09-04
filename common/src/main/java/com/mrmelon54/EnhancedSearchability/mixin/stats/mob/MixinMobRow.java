package com.mrmelon54.EnhancedSearchability.mixin.stats.mob;

import com.mrmelon54.EnhancedSearchability.duck.StatsEntryDuck;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StatsScreen.MobsStatisticsList.MobRow.class)
public class MixinMobRow implements StatsEntryDuck {
    @Shadow
    @Final
    private Component mobName;

    @Override
    public Component enhanced_searchability$getText() {
        return mobName;
    }
}
