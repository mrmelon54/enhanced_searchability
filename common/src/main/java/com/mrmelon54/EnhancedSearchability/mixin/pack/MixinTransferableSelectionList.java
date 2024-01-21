package com.mrmelon54.EnhancedSearchability.mixin.pack;

import com.mrmelon54.EnhancedSearchability.duck.PackEntryDuck;
import com.mrmelon54.EnhancedSearchability.duck.TransferableSelectionListDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

@Mixin(TransferableSelectionList.class)
public class MixinTransferableSelectionList extends ObjectSelectionList<TransferableSelectionList.PackEntry> implements TransferableSelectionListDuck {
    @Shadow
    @Final
    private Component title;
    @Unique
    private final List<TransferableSelectionList.PackEntry> enhanced_searchability$storeChildren = new ArrayList<>();

    public MixinTransferableSelectionList(Minecraft minecraft, int i, int j, int k, int l) {
        super(minecraft, i, j, k, l);
    }

    @Override
    public Component enhanced_searchability$getHeaderText() {
        return title;
    }

    @Override
    public void enhanced_searchability$filter(Supplier<String> searchTextSupplier) {
        String a = searchTextSupplier.get().toLowerCase(Locale.ROOT);

        if (a.trim().isEmpty()) {
            children().clear();
            enhanced_searchability$storeChildren.forEach(item -> children().add(item));
            return;
        }

        children().clear();
        enhanced_searchability$storeChildren.forEach(child -> {
            if (enhanced_searchability$hasMatchingName(child, a))
                children().add(child);
        });
    }

    @Unique
    boolean enhanced_searchability$hasMatchingName(TransferableSelectionList.PackEntry child, String a) {
        if (child instanceof PackEntryDuck duck) {
            PackSelectionModel.Entry pack = duck.enhanced_searchability$getPack();
            String title = pack.getTitle().getString().toLowerCase(Locale.ROOT);
            String desc = pack.getDescription().getString().toLowerCase(Locale.ROOT);
            return (a.isEmpty() || title.contains(a) || desc.contains(a));
        }
        return false;
    }

    @Override
    public List<TransferableSelectionList.PackEntry> enhanced_searchability$getSyncStoreRP() {
        return enhanced_searchability$storeChildren;
    }

    @Override
    public void enhanced_searchability$hideHeaderAndShift() {
        setY(getY() + headerHeight + 1 + 22);
        setHeight(getHeight() - headerHeight - 1 - 22);
        setRenderHeader(false, 0);
    }

    @Override
    public double getScrollAmount() {
        double v = super.getScrollAmount();
        int m = getMaxScroll();
        return v > m ? m : v;
    }

    @Override
    public int enhanced_searchability$getRowLeft() {
        return super.getRowLeft();
    }

    @Override
    public int enhanced_searchability$getRowWidth() {
        return super.getRowWidth();
    }
}
