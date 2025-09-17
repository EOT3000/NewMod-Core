package me.bergenfly.newmod.core.api.item.builder.modifiers;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
//import io.papermc.paper.datacomponent.item.PaperUnbreakable;
import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public record ArmorModifier(String plugin, String id, int armor, int toughness, int durability) implements MetaModifier {
    @Override
    public void apply(ItemStack stack) {

        if(durability > 0) {
            stack.setData(DataComponentTypes.MAX_DAMAGE, durability);
        } else {
            //stack.setData(DataComponentTypes.UNBREAKABLE);
        }

        ItemAttributeModifiers modifiers = ItemAttributeModifiers.itemAttributes()
                .addModifier(Attribute.ARMOR_TOUGHNESS, new AttributeModifier(new NamespacedKey(plugin, id), toughness, AttributeModifier.Operation.ADD_NUMBER))
                .addModifier(Attribute.ARMOR, new AttributeModifier(new NamespacedKey(plugin, id), armor, AttributeModifier.Operation.ADD_NUMBER))
                .build();

        stack.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, modifiers);
    }
}
