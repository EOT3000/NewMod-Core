package me.bergenfly.newmod.flyfun.food.nutrient;

import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import me.bergenfly.newmod.core.api.item.data.ModItemData;
import me.bergenfly.newmod.core.util.PersistentDataUtil;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ModFood extends Food implements ModItem {
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockManager blockManager = api.blockManager();
    private static final ItemManager itemManager = api.itemManager();

    private final NamespacedKey id;
    private final Material material;
    private final TextComponent component;

    private final ModBlock block;
    private final Class<? extends ModItemData> data;

    private final List<MetaModifier> modifiers;


    public ModFood(NamespacedKey id, Material material, TextComponent component,
                   ModBlock block, Class<? extends ModItemData> data, List<MetaModifier> modifiers,
                   int nutrition, float saturation, int size, int toughness) {
        super(nutrition, saturation, size, toughness, null);
        this.id = id;
        this.material = material;
        this.component = component;
        this.block = block;
        this.data = data;
        this.modifiers = modifiers;
    }

    @Override
    public NamespacedKey getId() {
        return id;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Component getDisplayName() {
        return component;
    }

    @Override
    public Class<? extends ModItemData> getDataType() {
        return data;
    }

    @Override
    public ModBlock getBlock() {
        return block;
    }

    @Override
    public ItemStack applyModifiers(ItemStack stack) {
        for(MetaModifier modifier : modifiers) {
            modifier.apply(stack);
        }

        return stack;
    }

    @Override
    public ItemStack create() {
        ItemStack stack = new ItemStack(material);

        applyModifiers(stack);

        itemManager.setType(stack, id);

        return stack;
    }

    @Override
    public void setIngredient(char c, ShapedRecipe recipe) {
        recipe.setIngredient(c, create());
    }
}
