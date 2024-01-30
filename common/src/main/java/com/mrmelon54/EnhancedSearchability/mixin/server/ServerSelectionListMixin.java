package com.mrmelon54.EnhancedSearchability.mixin.server;

import com.mrmelon54.EnhancedSearchability.EnhancedSearchability;
import com.mrmelon54.EnhancedSearchability.duck.FilterSupplier;
import com.mrmelon54.EnhancedSearchability.duck.HeaderHider;
import com.mrmelon54.EnhancedSearchability.duck.ListProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.server.LanServer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Mixin(ServerSelectionList.class)
public abstract class ServerSelectionListMixin extends ObjectSelectionList<ServerSelectionList.Entry> implements ListProvider, FilterSupplier, HeaderHider {
    @Shadow
    @Final
    private List<ServerSelectionList.OnlineServerEntry> onlineServers;
    @Shadow
    @Final
    private List<ServerSelectionList.NetworkServerEntry> networkServers;
    @Shadow
    @Final
    private ServerSelectionList.Entry lanHeader;

    @Unique
    private final List<ServerSelectionList.OnlineServerEntry> enhanced_searchability$onlineServerSyncStore = new ArrayList<>();
    @Unique
    private final List<ServerSelectionList.NetworkServerEntry> enhanced_searchability$networkServerSyncStore = new ArrayList<>();
    @Unique
    private Supplier<String> enhanced_searchability$searchTextStore = () -> "";

    public ServerSelectionListMixin(Minecraft minecraft, int i, int j, int k, int l) {
        super(minecraft, i, j, k, l);
    }

    @Override
    public void enhanced_searchability$filter(Supplier<String> searchTextSupplier) {
        if (EnhancedSearchability.isServersDisabled()) return;
        enhanced_searchability$searchTextStore = searchTextSupplier;
        enhanced_searchability$customAddServerStreamToUI(enhanced_searchability$onlineServerSyncStore.stream(), enhanced_searchability$networkServerSyncStore.stream(), enhanced_searchability$searchTextStore);
    }

    @Inject(method = "refreshEntries", at = @At("TAIL"))
    private void injected_updateEntries(CallbackInfo ci) {
        if (EnhancedSearchability.isServersDisabled()) return;
        enhanced_searchability$customAddServerStreamToUI(enhanced_searchability$onlineServerSyncStore.stream(), enhanced_searchability$networkServerSyncStore.stream(), enhanced_searchability$searchTextStore);
    }

    @Override
    public void enhanced_searchability$hideHeaderAndShift() {
        setY(getY() + 15);
        setHeight(getHeight() - 15);
    }

    @Override
    public double getScrollAmount() {
        double v = super.getScrollAmount();
        int m = getMaxScroll();
        return v > m ? m : v;
    }

    @Override
    public int enhanced_searchability$getRowLeft() {
        return getRowLeft();
    }

    @Override
    public int enhanced_searchability$getRowWidth() {
        return getRowWidth();
    }

    @Redirect(method = "updateOnlineServers", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/multiplayer/ServerSelectionList;onlineServers:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ServerSelectionList.OnlineServerEntry> redirectOnlineServers(ServerSelectionList instance) {
        return EnhancedSearchability.isServersDisabled() ? onlineServers : enhanced_searchability$onlineServerSyncStore;
    }

    @Redirect(method = "updateNetworkServers", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/multiplayer/ServerSelectionList;networkServers:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ServerSelectionList.NetworkServerEntry> redirectNetworkServers(ServerSelectionList instance) {
        return EnhancedSearchability.isServersDisabled() ? networkServers : enhanced_searchability$networkServerSyncStore;
    }

    @Unique
    private void enhanced_searchability$customAddServerStreamToUI(Stream<ServerSelectionList.OnlineServerEntry> onlineStream, Stream<ServerSelectionList.NetworkServerEntry> networkStream, Supplier<String> search) {
        String s = search.get().toLowerCase(Locale.ROOT);

        // clear before filling
        children().clear();

        // online servers
        onlineStream.forEach(onlineServerEntry -> {
            ServerData serverData = onlineServerEntry.getServerData();
            String name = serverData.name.toLowerCase(Locale.ROOT);
            //noinspection ConstantValue - this value can be null
            String motd = serverData.motd != null ? serverData.motd.getString().toLowerCase(Locale.ROOT) : "";
            if (s.isEmpty() || name.contains(s) || motd.contains(s)) children().add(onlineServerEntry);
        });

        // network/lan servers
        children().add(lanHeader);
        networkStream.forEach(networkServerEntry -> {
            LanServer serverData = networkServerEntry.getServerData();
            String address = serverData.getAddress().toLowerCase(Locale.ROOT);
            //noinspection ConstantValue - this value can be null
            String motd = serverData.getMotd() != null ? serverData.getMotd().toLowerCase(Locale.ROOT) : "";
            if (s.isEmpty() || address.contains(s) || motd.contains(s)) children().add(networkServerEntry);
        });
    }
}
