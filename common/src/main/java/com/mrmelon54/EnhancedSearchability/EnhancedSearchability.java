package com.mrmelon54.EnhancedSearchability;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Supplier;

public class EnhancedSearchability {
    public static final String MOD_ID = "enhanced_searchability";
    private static ConfigStructure config;

    public static ConfigStructure getConfig() {
        return config;
    }

    public static void init() {
        AutoConfig.register(ConfigStructure.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ConfigStructure.class).getConfig();
    }

    public static Supplier<Screen> createConfigScreen(Screen screen) {
        return AutoConfig.getConfigScreen(ConfigStructure.class, screen);
    }

    public static boolean isPacksDisabled() {
        return !EnhancedSearchability.getConfig().resourcePacksEnabled;
    }

    public static boolean isServersDisabled() {
        return !EnhancedSearchability.getConfig().serversEnabled;
    }

    public static boolean isStatsDisabled() {
        return !EnhancedSearchability.getConfig().statsEnabled;
    }
}
