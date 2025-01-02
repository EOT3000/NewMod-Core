package me.bergenfly.newmod.core.item;

import me.bergenfly.newmod.core.NewModPlugin;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.core.api.item.ModItem;
import me.bergenfly.newmod.core.api.item.ModItemStack;
import me.bergenfly.newmod.core.api.item.data.ModItemData;
import me.bergenfly.newmod.core.util.PersistentDataUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ModItemStackImpl implements ModItemStack {
    private final ItemStack representation;

    private final ModItem type;
    private ModItemData data;
    private int amount;

    public static ModItemStackImpl makeOrNull(ItemStack stack) {
        ItemManager manager = NewModPlugin.get().itemManager();

        if(manager.getModType(stack) == null) {
            return null;
        }

        return new ModItemStackImpl(stack);
    }

    public ModItemStackImpl(ItemStack representation) {
        ItemManager manager = NewModPlugin.get().itemManager();

        this.representation = representation;

        this.type = manager.getModType(representation);
        this.data = manager.getData(representation);
        this.amount = representation.getAmount();
    }

    public ModItemStackImpl(ModItem type) {
        ItemManager manager = NewModPlugin.get().itemManager();

        this.type = type;
        this.data = manager.createDefaultData(type);
        this.amount = 1;

        ItemStack stack = new ItemStack(type.getMaterial(), amount);
        ItemMeta itemMeta = stack.getItemMeta();

        itemMeta.getPersistentDataContainer().set(ItemManagerImpl.ID, PersistentDataUtil.NAMESPACED_KEY, type.getId());

        stack.setItemMeta(itemMeta);

        manager.applyData(stack, data);

        type.applyModifiers(stack);

        this.representation = stack;
    }

    public ModItemStackImpl(ModItem type, ModItemData data, ItemStack representation) {
        this.representation = representation;

        this.type = type;
        this.data = data;
        this.amount = representation.getAmount();
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        representation.setAmount(amount);

        this.amount = representation.getAmount();
    }

    @Override
    public ModItem getType() {
        return type;
    }

    @Override
    public ModItemData getData() {
        return data.cloneItem();
    }

    @Override
    public void setData(ModItemData data) {
        this.data = data;
    }

    @Override
    public void update() {
        ItemManager manager = NewModPlugin.get().itemManager();

        manager.applyData(representation, data);
    }
}
