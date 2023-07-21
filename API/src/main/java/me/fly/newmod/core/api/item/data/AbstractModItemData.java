package me.fly.newmod.core.api.item.data;

import me.fly.newmod.core.api.item.ModItem;

public abstract class AbstractModItemData implements ModItemData {
    private final ModItem item;

    protected AbstractModItemData(ModItem item) {
        this.item = item;
    }

    @Override
    public ModItem getType() {
        return item;
    }
}
