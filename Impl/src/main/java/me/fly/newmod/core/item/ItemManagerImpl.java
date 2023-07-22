package me.fly.newmod.core.item;

import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.item.ItemManager;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.api.item.ModItemStack;
import me.fly.newmod.core.api.item.builder.ModItemBuilder;
import me.fly.newmod.core.api.item.data.ModItemData;
import me.fly.newmod.core.api.item.data.ModItemDataSerializer;
import me.fly.newmod.core.api.util.PersistentDataUtil;
import me.fly.newmod.core.item.builder.ModItemBuilderImpl;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ItemManagerImpl implements ItemManager {
    public static final NamespacedKey ID = new NamespacedKey(NewModPlugin.get(), "id");

    private final Map<Class<? extends ModItemData>, ModItemDataSerializer<?>> serializers = new HashMap<>();
    private final Map<NamespacedKey, ModItem> registry = new LinkedHashMap<>();

    @Override
    public void registerItem(ModItem item) {
        registry.put(item.getId(), item);
    }

    @Override
    public ModItem getType(ItemStack stack) {
        if(stack == null || !stack.hasItemMeta()) {
            return null;
        }

        NamespacedKey id = stack.getItemMeta().getPersistentDataContainer().get(ID, PersistentDataUtil.NAMESPACED_KEY);

        return registry.get(id);
    }

    @Override
    public ModItem getType(NamespacedKey key) {
        return registry.get(key);
    }

    @Override
    public <T extends ModItemData> void registerSerializer(ModItemDataSerializer<? extends T> serializer, Class<T> clazz) {
        serializers.put(clazz, serializer);
    }

    @Override
    public ModItemStack getStack(ItemStack stack) {
        return ModItemStackImpl.makeOrNull(stack);
    }

    @Override
    public ModItemStack getStack(ModItem item) {
        if(item == null) {
            return null;
        }

        return new ModItemStackImpl(item);
    }

    @Override
    public <T extends ModItemData> T createDefaultData(ModItem type) {
        //noinspection unchecked
        return (T) serializers.get(type.getDataType()).createDefaultData(type);
    }

    @Override
    public ModItemData getData(ItemStack stack) {
        ModItem type = getType(stack);

        if(type == null) {
            return null;
        }

        Class<? extends ModItemData> clazz = type.getDataType();

        return serializers.get(clazz).getData(stack);
    }

    @Override
    public ModItemBuilder createBuilder(Material material, JavaPlugin plugin, String id) {
        return new ModItemBuilderImpl(material, new NamespacedKey(plugin, id));
    }

    @Override
    public boolean applyData(ItemStack stack, ModItemData data) {
        ModItem type = getType(stack);

        if(type == null) {
            return false;
        }

        ModItemDataSerializer<?> serializer = serializers.get(type.getDataType());

        if(!serializer.canSerialize(data)) {
            return false;
        }

        serializer.applyData(stack, data);

        return true;
    }
}
