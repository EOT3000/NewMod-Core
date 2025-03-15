package me.bergenfly.newmod.core.api.gear;

import me.bergenfly.newmod.core.api.item.ModArmor;
import me.bergenfly.newmod.core.api.item.ModItem;
import org.jetbrains.annotations.Nullable;

public interface ArmorSet {
    @Nullable default ModArmor getHelmet() {
        return getSection(GearManager.ArmorSection.HELMET);
    }
    @Nullable default ModArmor getChestplate() {
        return getSection(GearManager.ArmorSection.CHESTPLATE);
    }
    @Nullable default ModArmor getLeggings() {
        return getSection(GearManager.ArmorSection.LEGGINGS);
    }
    @Nullable default ModArmor getBoots() {
        return getSection(GearManager.ArmorSection.BOOTS);
    }

    @Nullable ModArmor getSection(GearManager.ArmorSection section);
}
