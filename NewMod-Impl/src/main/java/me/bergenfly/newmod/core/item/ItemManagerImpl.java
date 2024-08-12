package me.bergenfly.newmod.core.item;

import me.bergenfly.newmod.core.NewModPlugin;
import me.bergenfly.newmod.core.api.gear.DurabilityController;
import me.bergenfly.newmod.core.api.gear.GearManager;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.ModItemStack;
import me.bergenfly.newmod.core.api.item.builder.ModItemBuilder;
import me.bergenfly.newmod.core.api.item.data.ModItemData;
import me.bergenfly.newmod.core.api.item.data.ModItemDataSerializer;
import me.bergenfly.newmod.core.util.PersistentDataUtil;
import me.bergenfly.newmod.core.item.builder.ModItemBuilderImpl;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ItemManagerImpl implements ItemManager, GearManager {
    public static final NamespacedKey ID = new NamespacedKey(NewModPlugin.get(), "id");
    public static final NamespacedKey MAX_DURA = new NamespacedKey(NewModPlugin.get(), "max_durability");
    public static final NamespacedKey DAMAGE = new NamespacedKey(NewModPlugin.get(), "damage");

    private final Map<Class<? extends ModItemData>, ModItemDataSerializer<?>> serializers = new HashMap<>();
    private final Map<NamespacedKey, ModItem> registry = new LinkedHashMap<>();

    private final Map<ModItem, DurabilityController> durabilityControllers = new HashMap<>();
    private final Map<ModItem, Integer> miningLevel = new HashMap<>();

    @Override
    public void registerItem(ModItem item) {
        registry.put(item.getId(), item);

        NewModPlugin.get().getCategory().addItem(item);
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

    @Override
    public DurabilityController getController(ModItem item) {
        return durabilityControllers.get(item);
    }

    @Override
    public void setController(ModItem item, DurabilityController controller) {
        durabilityControllers.put(item, controller);
    }

    @Override
    public void setMiningLevel(ModItem item, int level) {
        miningLevel.put(item, level);
    }

    @Override
    public int getMiningLevel(ModItem item) {
        return miningLevel.get(item);
    }

    @Override
    public int getMaxDurability(ItemStack stack) {
        int md = stack.getItemMeta().getPersistentDataContainer().getOrDefault(MAX_DURA, PersistentDataType.INTEGER, -1);

        if(md == -1) {
            md = stack.getType().getMaxDurability();
        }

        if(md <= 0) {
            return -1;
        }

        return md;
    }

    @Override
    public void setMaxDurability(ItemStack stack, int durability) {
        ItemMeta meta = stack.getItemMeta();

        meta.getPersistentDataContainer().set(MAX_DURA, PersistentDataType.INTEGER, durability);

        stack.setItemMeta(meta);
    }

    @Override
    public int getDamage(ItemStack stack) {
        int d = stack.getItemMeta().getPersistentDataContainer().getOrDefault(DAMAGE, PersistentDataType.INTEGER, -1);

        if(d == -1 && stack.getItemMeta() instanceof Damageable) {
            d = ((Damageable) stack.getItemMeta()).getDamage();
        }

        if(d < 0) {
            return -1;
        }

        return d;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        ItemMeta meta = stack.getItemMeta();

        meta.getPersistentDataContainer().set(DAMAGE, PersistentDataType.INTEGER, damage);

        stack.setItemMeta(meta);

        double ratio = stack.getType().getMaxDurability() / (double) getMaxDurability(stack);

        stack.setDamage((int) Math.ceil(damage * ratio));
    }
}
