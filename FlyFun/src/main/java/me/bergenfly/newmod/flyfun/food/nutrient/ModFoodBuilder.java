package me.bergenfly.newmod.flyfun.food.nutrient;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.block.BasicBlock;
import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.builder.ModItemBuilder;
import me.bergenfly.newmod.core.api.item.builder.meta.MetaModifier;
import me.bergenfly.newmod.core.api.item.builder.modifiers.ColorModifier;
import me.bergenfly.newmod.core.api.item.builder.modifiers.DisplayNameModifier;
import me.bergenfly.newmod.core.api.item.builder.modifiers.EnchantmentModifier;
import me.bergenfly.newmod.core.api.item.builder.modifiers.LoreModifier;
import me.bergenfly.newmod.core.api.item.category.ModItemCategory;
import me.bergenfly.newmod.core.api.item.data.ModItemData;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.food.listener.FoodListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModFoodBuilder implements ModItemBuilder {
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockManager blockManager = api.blockManager();
    private static final ItemManager itemManager = api.itemManager();

    private final Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
    private final List<TextComponent> lore = new ArrayList<>();
    private final List<MetaModifier> modifiers = new ArrayList<>();

    private TextComponent displayName;
    private ModBlock block;
    private Color color;
    private Class<? extends ModItemData> data;

    private final Material material;
    private final NamespacedKey key;

    private ModItemCategory category;

    private int nutrition;
    private float saturation;
    private int size;
    private int toughness;

    public ModFoodBuilder(Material material, JavaPlugin plugin, String id) {
        this.material = material;
        this.key = new NamespacedKey(plugin, id);
    }

    public ModFoodBuilder food(int nutrition, float saturation, int size, int toughness) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.size = size;
        this.toughness = toughness;

        addModifier(new MetaModifier() {
            @Override
            public void apply(ItemStack stack) {
                FoodProperties newFP = FoodProperties.food()
                        .nutrition(nutrition)
                        .saturation(saturation)
                        .build();

                Consumable newC = Consumable.consumable()
                        .consumeSeconds(Food.calculateConsumeTimeSeconds(toughness, size)).
                        build();

                stack.setData(DataComponentTypes.FOOD, newFP);
                stack.setData(DataComponentTypes.CONSUMABLE, newC);

                ItemMeta meta = stack.getItemMeta();

                meta.getPersistentDataContainer().set(FoodListener.CUSTOM_FOOD, PersistentDataType.STRING, "set");

                stack.setItemMeta(meta);
            }
        });

        return this;
    }

    @Override
    public ModFoodBuilder displayName(TextComponent component) {
        this.displayName = component;

        return this;
    }

    @Override
    public ModFoodBuilder displayName(String string, TextColor color) {
        return displayName(Component.text(string).color(color).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public ModFoodBuilder displayName(String string, int color) {
        return displayName(string, TextColor.color(color));
    }


    @Override
    public ModFoodBuilder color(Color color) {
        this.color = color;

        return this;
    }

    @Override
    public ModFoodBuilder addEnchantment(Enchantment enchantment, int lvl) {
        enchantments.put(enchantment, lvl);

        return this;
    }

    @Override
    public ModFoodBuilder addLore(TextComponent component) {
        lore.add(component);

        return this;
    }

    @Override
    public ModFoodBuilder addModifier(MetaModifier modifier) {
        modifiers.add(modifier);

        return this;
    }

    @Override
    public ModFoodBuilder block(ModBlock block) {
        this.block = block;

        return this;
    }

    @Override
    public ModFoodBuilder dataType(Class<? extends ModItemData> clazz) {
        this.data = clazz;

        return this;
    }

    @Override
    public ModFoodBuilder category(ModItemCategory category) {
        this.category = category;

        return this;
    }

    @Override
    public ModFood build() {
        if(displayName == null) {
            displayName = Component.text("");
        }

        modifiers.add(new DisplayNameModifier(displayName));

        if(color != null) {
            modifiers.add(new ColorModifier(color));
        }

        for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            modifiers.add(new EnchantmentModifier(entry.getKey(), entry.getValue()));
        }

        if(lore.size() != 0) {
            modifiers.add(new LoreModifier(lore));
        }

        ModFood item = new ModFood(key, material, displayName, block, data, modifiers, nutrition, saturation, size, toughness);

        if(category != null) {
            category.addItem(item);
        }

        BlockManager bm = blockManager;

        if(block != null && bm.getType(block.getId()) == null) {
            bm.registerBlock(block);

            if(block instanceof BasicBlock) {
                ((BasicBlock) block).setDrop(item.create());
            }
        }

        itemManager.registerItem(item);

        System.out.println("Item built: " + key.toString());

        return item;
    }
}
