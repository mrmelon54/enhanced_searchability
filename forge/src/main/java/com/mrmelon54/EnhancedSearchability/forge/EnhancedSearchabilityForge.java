package com.mrmelon54.EnhancedSearchability.forge;

import dev.architectury.platform.forge.EventBuses;
import com.mrmelon54.EnhancedSearchability.EnhancedSearchability;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EnhancedSearchability.MOD_ID)
public class EnhancedSearchabilityForge {
    public EnhancedSearchabilityForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(EnhancedSearchability.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        EnhancedSearchability.init();
    }
}
