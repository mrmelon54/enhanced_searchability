package com.mrmelon54.EnhancedSearchability.duck;

import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface TransferableSelectionListDuck extends ListProvider, FilterSupplier, HeaderHider {
    Component enhanced_searchability$getHeaderText();

    List<TransferableSelectionList.PackEntry> enhanced_searchability$getSyncStoreRP();
}
