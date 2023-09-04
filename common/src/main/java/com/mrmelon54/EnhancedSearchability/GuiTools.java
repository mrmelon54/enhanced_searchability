package com.mrmelon54.EnhancedSearchability;

import com.mrmelon54.EnhancedSearchability.duck.FilterSupplier;
import com.mrmelon54.EnhancedSearchability.duck.ListProvider;
import com.mrmelon54.EnhancedSearchability.duck.StatsEntryDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class GuiTools {
    public static EditBox addSearchBox(Minecraft mc, WidgetAdder addWidget, int y, ListProvider list, FilterSupplier filter, EditBox searchBox) {
        searchBox = new EditBox(mc.font, list.getRowLeft() - 1, y, list.getRowWidth() - 2, 20, searchBox, Component.translatable("enhanced_searchability.searchBox"));
        searchBox.setResponder(search -> filter.enhanced_searchability$filter(() -> search));
        addWidget.addWidget(searchBox);
        return searchBox;
    }

    public static <T extends ObjectSelectionList.Entry<?>> void enhanced_searchability$customAddEntriesToUI(Supplier<String> searchSupplier, EntryClearer entryClearer, List<T> original, EntryAdder<T> entryAdder) {
        String s = searchSupplier.get().toLowerCase(Locale.ROOT);

        // clear before filling
        entryClearer.enhanced_searchability$parentClearEntries();

        original.forEach(entry -> {
            if (entry instanceof StatsEntryDuck duck) {
                String mob = duck.enhanced_searchability$getText().getString().toLowerCase(Locale.ROOT);
                if (s.isEmpty() || mob.contains(s)) entryAdder.enhanced_searchability$parentAddEntry(entry);
            }
        });
    }

    public interface WidgetAdder {
        void addWidget(EditBox box);
    }

    public interface EntryClearer {
        void enhanced_searchability$parentClearEntries();
    }

    public interface EntryAdder<T extends ObjectSelectionList.Entry<?>> {
        void enhanced_searchability$parentAddEntry(T entry);
    }
}
