package com.mrmelon54.EnhancedSearchability.duck;

import java.util.function.Supplier;

public interface FilterSupplier {
    void enhanced_searchability$filter(Supplier<String> searchTextSupplier);
}
