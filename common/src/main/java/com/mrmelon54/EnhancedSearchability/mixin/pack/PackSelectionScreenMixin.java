package com.mrmelon54.EnhancedSearchability.mixin.pack;

import com.mrmelon54.EnhancedSearchability.EnhancedSearchability;
import com.mrmelon54.EnhancedSearchability.GuiTools;
import com.mrmelon54.EnhancedSearchability.duck.FilterSupplier;
import com.mrmelon54.EnhancedSearchability.duck.HeaderHider;
import com.mrmelon54.EnhancedSearchability.duck.ListProvider;
import com.mrmelon54.EnhancedSearchability.duck.TransferableSelectionListDuck;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Stream;

@Mixin(PackSelectionScreen.class)
public class PackSelectionScreenMixin extends Screen {
    @Shadow
    private TransferableSelectionList availablePackList;
    @Shadow
    private TransferableSelectionList selectedPackList;
    @Shadow
    @Final
    private PackSelectionModel model;
    @Shadow
    private Button doneButton;
    @Unique
    private EditBox enhanced_searchability$availablePackSearchBox;
    @Unique
    private EditBox enhanced_searchability$selectedPackSearchBox;

    protected PackSelectionScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/packs/PackSelectionScreen;reload()V"))
    private void injected_init(CallbackInfo ci) {
        if (EnhancedSearchability.isPacksDisabled()) return;

        Minecraft mc = Minecraft.getInstance();
        if (availablePackList instanceof ListProvider listDuck && availablePackList instanceof FilterSupplier filterDuck && availablePackList instanceof HeaderHider hiderDuck) {
            //noinspection Convert2MethodRef - these conversions break the mod,
            enhanced_searchability$availablePackSearchBox = GuiTools.addSearchBox(mc, (EditBox box) -> addWidget(box), 47, listDuck, filterDuck, enhanced_searchability$availablePackSearchBox);
            hiderDuck.enhanced_searchability$hideHeaderAndShift();
        }
        if (selectedPackList instanceof ListProvider listDuck && selectedPackList instanceof FilterSupplier filterDuck && selectedPackList instanceof HeaderHider hiderDuck) {
            //noinspection Convert2MethodRef - these conversions break the mod,
            enhanced_searchability$selectedPackSearchBox = GuiTools.addSearchBox(mc, (EditBox box) -> addWidget(box), 47, listDuck, filterDuck, enhanced_searchability$selectedPackSearchBox);
            hiderDuck.enhanced_searchability$hideHeaderAndShift();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void injected_render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (EnhancedSearchability.isPacksDisabled()) return;

        if (enhanced_searchability$availablePackSearchBox != null)
            enhanced_searchability$availablePackSearchBox.render(guiGraphics, i, j, f);
        if (enhanced_searchability$selectedPackSearchBox != null)
            enhanced_searchability$selectedPackSearchBox.render(guiGraphics, i, j, f);

        Minecraft mc = Minecraft.getInstance();
        enhanced_searchability$renderOverlayHeader(guiGraphics, mc, availablePackList);
        enhanced_searchability$renderOverlayHeader(guiGraphics, mc, selectedPackList);
    }

    @Unique
    void enhanced_searchability$renderOverlayHeader(GuiGraphics guiGraphics, Minecraft mc, TransferableSelectionList transferableSelectionList) {
        int left = transferableSelectionList.getRowLeft() - 2;
        int w = transferableSelectionList.getRowWidth();

        Component text1 = transferableSelectionList instanceof TransferableSelectionListDuck duck ? duck.enhanced_searchability$getHeaderText() : Component.empty();
        Component text = text1.plainCopy().withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD);
        guiGraphics.drawString(mc.font, text, (left + w / 2 - mc.font.width(text) / 2), 35, 0xffffff);
    }

    @Inject(method = "populateLists", at = @At("HEAD"), cancellable = true)
    private void injected_populateLists(CallbackInfo ci) {
        if (EnhancedSearchability.isPacksDisabled() || minecraft == null) return;
        if (availablePackList instanceof TransferableSelectionListDuck duck && enhanced_searchability$availablePackSearchBox != null) {
            enhanced_searchability$customUpdatePackList(minecraft, availablePackList, duck, model.getUnselected());
            duck.enhanced_searchability$filter(() -> enhanced_searchability$availablePackSearchBox.getValue());
        }
        if (selectedPackList instanceof TransferableSelectionListDuck duck && enhanced_searchability$selectedPackSearchBox != null) {
            enhanced_searchability$customUpdatePackList(minecraft, selectedPackList, duck, model.getSelected());
            duck.enhanced_searchability$filter(() -> enhanced_searchability$selectedPackSearchBox.getValue());
            doneButton.active = !duck.enhanced_searchability$getSyncStoreRP().isEmpty();
        }
        ci.cancel();
    }

    @Unique
    private void enhanced_searchability$customUpdatePackList(Minecraft minecraft, TransferableSelectionList widget, TransferableSelectionListDuck duck, Stream<PackSelectionModel.Entry> packs) {
        List<TransferableSelectionList.PackEntry> packEntries = duck.enhanced_searchability$getSyncStoreRP();
        packEntries.clear();
        packs.forEach(pack -> packEntries.add(new TransferableSelectionList.PackEntry(minecraft, widget, pack)));
    }
}
