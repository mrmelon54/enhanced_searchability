package com.mrmelon54.EnhancedSearchability.duck;

import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

public interface TransferableSelectionListDuck {
    Component getHeaderText();

    void filter(Supplier<String> searchTextSupplier);

    List<TransferableSelectionList.PackEntry> getSyncStoreRP();

    void hideHeaderAndShift();
}
