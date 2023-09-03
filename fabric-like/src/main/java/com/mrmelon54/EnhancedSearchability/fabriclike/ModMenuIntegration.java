package com.mrmelon54.EnhancedSearchability.fabriclike;

import com.mrmelon54.EnhancedSearchability.EnhancedSearchability;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> EnhancedSearchability.createConfigScreen(parent).get();
    }
}
