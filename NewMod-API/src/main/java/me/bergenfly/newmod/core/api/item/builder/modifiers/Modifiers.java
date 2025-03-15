package me.bergenfly.newmod.core.api.item.builder.modifiers;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import org.bukkit.inventory.ItemStack;

public class Modifiers {
    public static final MetaModifier NOT_CONSUMABLE = new MetaModifier() {
        @Override
        public void apply(ItemStack stack) {
            stack.unsetData(DataComponentTypes.CONSUMABLE);
        }
    };
}
