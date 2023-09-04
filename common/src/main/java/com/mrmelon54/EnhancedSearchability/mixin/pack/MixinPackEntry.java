package com.mrmelon54.EnhancedSearchability.mixin.pack;

import com.mrmelon54.EnhancedSearchability.duck.PackEntryDuck;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TransferableSelectionList.PackEntry.class)
public class MixinPackEntry implements PackEntryDuck {
    @Shadow
    @Final
    private PackSelectionModel.Entry pack;

    @Override
    public PackSelectionModel.Entry enhanced_searchability$getPack() {
        return pack;
    }
}
