package com.mrmelon54.EnhancedSearchability.fabric;

import com.mrmelon54.EnhancedSearchability.fabriclike.EnhancedSearchabilityFabricLike;
import net.fabricmc.api.ModInitializer;

public class EnhancedSearchabilityFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        EnhancedSearchabilityFabricLike.init();
    }
}
