package me.bergenfly.newmod.villagers.crafting;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.gear.GearManager;
import me.bergenfly.newmod.core.api.item.ModItem;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.IntSupplier;

public class CraftableItems {


    public enum Workstation {
        ANVIL,
        SMITHING_TABLE,

    }

    public static class CraftableItem {
        private static NewModAPI api = null;

        private final ItemStack result;
        private final IntSupplier durability;
        private final Object2IntMap<Object> requirements;

        public CraftableItem(ItemStack result, IntSupplier durability, Object... ingredients) {
            this.result = result;
            this.durability = durability;

            this.requirements = new Object2IntOpenHashMap<>();

            for(Object object : ingredients) {
                this.requirements.put(object, this.requirements.getOrDefault(object, 0)+1);
            }
        }

        public Object2IntMap<Object> requirements() {
            return new Object2IntOpenHashMap<>(requirements);
        }

        public boolean hasRequirements(Inventory inventory) {
            for(Object object : this.requirements.keySet()) {
                if(object instanceof ModItem item) {
                    if(!inventory.contains(item.create(), this.requirements.get(item))) {
                        return false;
                    }
                } else if (object instanceof Material mat) {
                    if(!inventory.contains(mat, this.requirements.get(mat))) {
                        return false;
                    }
                }
            }

            return true;
        }

        public ItemStack createResult() {
            return durability == null ? new ItemStack(result) : setDurability(result);
        }

        private ItemStack setDurability(ItemStack stack) {
            ItemStack ret = new ItemStack(stack);

            GearManager gm = api.gearManager();
            gm.setMaxDurability(ret, durability.getAsInt());

            return ret;
        }
    }
}
