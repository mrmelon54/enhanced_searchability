package com.mrmelon54.EnhancedSearchability.quilt;

import com.mrmelon54.EnhancedSearchability.fabriclike.EnhancedSearchabilityFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class EnhancedSearchabilityQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        EnhancedSearchabilityFabricLike.init();
    }
}
