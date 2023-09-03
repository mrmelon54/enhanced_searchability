package com.mrmelon54.EnhancedSearchability.mixin.pack;

import com.mrmelon54.EnhancedSearchability.EnhancedSearchability;
import com.mrmelon54.EnhancedSearchability.duck.TransferableSelectionListDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackSelectionScreen.class)
public class MixinPackSelectionScreen extends Screen {
    @Shadow
    private TransferableSelectionList availablePackList;
    @Shadow
    private TransferableSelectionList selectedPackList;
    private EditBox availablePackSearchBox;
    private EditBox selectedPackSearchBox;

    protected MixinPackSelectionScreen(Component component) {
        super(component);
    }

    @Unique
    static boolean isEnabled() {
        return EnhancedSearchability.getConfig().resourcePacksEnabled;
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/packs/PackSelectionScreen;reload()V"))
    private void injected_init(CallbackInfo ci) {
        if (isEnabled()) return;

        Minecraft mc = Minecraft.getInstance();
        availablePackSearchBox = addSearchBox(mc, availablePackList, availablePackSearchBox);
        selectedPackSearchBox = addSearchBox(mc, selectedPackList, selectedPackSearchBox);
    }

    @Unique
    private EditBox addSearchBox(Minecraft mc, TransferableSelectionList availablePackList, EditBox availablePackSearchBox) {
        availablePackSearchBox = new EditBox(mc.font, availablePackList.getRowLeft() - 1, 47, availablePackList.getRowWidth() - 2, 20, availablePackSearchBox, Component.translatable("enhanced_searchability.searchBox"));
        availablePackSearchBox.setResponder(search -> {
            if (availablePackList instanceof TransferableSelectionListDuck duck)
                duck.filter(() -> search);
        });
        addWidget(availablePackSearchBox);
        return availablePackSearchBox;
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void injected_render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (isEnabled()) return;

        availablePackSearchBox.render(guiGraphics, i, j, f);
        selectedPackSearchBox.render(guiGraphics, i, j, f);
    }
}
