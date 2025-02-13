package me.bergenfly.newmod.core.api.gear;

import me.bergenfly.newmod.core.api.item.ModItem;
import org.jetbrains.annotations.Nullable;

public interface ArmorSet {
    @Nullable ModItem getHelmet();
    @Nullable ModItem getChestplate();
    @Nullable ModItem getLeggings();
    @Nullable ModItem getBoots();
}
