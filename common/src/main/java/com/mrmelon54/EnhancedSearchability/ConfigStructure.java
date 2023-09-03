package com.mrmelon54.EnhancedSearchability;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "enhanced_searchability")
@Config.Gui.Background("minecraft:textures/block/soul_sand.png")
public class ConfigStructure implements ConfigData {
    public boolean resourcePacksEnabled = true;
    public boolean serversEnabled = true;
    public boolean statsEnabled = true;
    public boolean worldsEnabled = true;
}
