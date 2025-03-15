package me.bergenfly.newmod.core.armor;

import me.bergenfly.newmod.core.api.gear.ArmorSet;
import me.bergenfly.newmod.core.api.gear.GearManager;
import me.bergenfly.newmod.core.api.item.ModArmor;
import me.bergenfly.newmod.core.api.item.ModItem;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public class ArmorSetImpl implements ArmorSet {
    private final EnumMap<GearManager.ArmorSection, ModArmor> armor = new EnumMap(GearManager.ArmorSection.class);

    public ArmorSetImpl(ModArmor head, ModArmor chest, ModArmor legs, ModArmor feet) {
        armor.put(GearManager.ArmorSection.HELMET, head);
        armor.put(GearManager.ArmorSection.CHESTPLATE, chest);
        armor.put(GearManager.ArmorSection.LEGGINGS, legs);
        armor.put(GearManager.ArmorSection.BOOTS, feet);
    }

    @Override
    public @Nullable ModArmor getSection(GearManager.ArmorSection section) {
        return armor.get(section);
    }
}
