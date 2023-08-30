package me.fly.newmod.flyfun.magic.block.altar;

import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.blockstorage.StoredBlock;
import me.fly.newmod.core.util.PersistentDataUtil;
import me.fly.newmod.flyfun.FlyFunPlugin;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.UUID;

public final class Pedestal {
    public static final NamespacedKey PEDESTAL = new NamespacedKey(FlyFunPlugin.get(), "item_pedestal");
    public static final NamespacedKey ITEM_ENTITY_ID_LSB = new NamespacedKey(FlyFunPlugin.get(), "item_entity_id_lsb");
    public static final NamespacedKey ITEM_ENTITY_ID_MSB = new NamespacedKey(FlyFunPlugin.get(), "item_entity_id_msb");
    public static final NamespacedKey DISPLAY_ENTITY_ID_LSB = new NamespacedKey(FlyFunPlugin.get(), "display_entity_id_lsb");
    public static final NamespacedKey DISPLAY_ENTITY_ID_MSB = new NamespacedKey(FlyFunPlugin.get(), "display_entity_id_msb");

    public static void setItemDisplay(ItemStack stack, Location pedestal) {
        Item item = pedestal.getWorld().dropItem(pedestal.clone().add(0.5, 1.2, 0.5), stack);

        item.setCanPlayerPickup(false);
        item.setCanMobPickup(false);

        item.setImmuneToCactus(true);
        item.setImmuneToExplosion(true);
        item.setImmuneToFire(true);
        item.setImmuneToLightning(true);

        item.setUnlimitedLifetime(true);

        item.setGravity(false);

        PersistentDataContainer container = item.getPersistentDataContainer();

        container.set(PEDESTAL, PersistentDataUtil.LOCATION, pedestal);

        StoredBlock block = FlyFunPlugin.get().api.blockStorage().getBlock(pedestal);

        block.setData(ITEM_ENTITY_ID_LSB, String.valueOf(item.getUniqueId().getLeastSignificantBits()), BlockStorage.StorageType.BLOCK_DATA);
        block.setData(ITEM_ENTITY_ID_MSB, String.valueOf(item.getUniqueId().getMostSignificantBits()), BlockStorage.StorageType.BLOCK_DATA);
    }

    public static void removeItemDisplay(Location pedestal) {
        StoredBlock block = FlyFunPlugin.get().api.blockStorage().getBlock(pedestal);

        NamespacedKey lsb_nk = ITEM_ENTITY_ID_LSB;
        NamespacedKey msb_nk = ITEM_ENTITY_ID_MSB;

        if(block.hasData(msb_nk, BlockStorage.StorageType.BLOCK_DATA) && block.hasData(lsb_nk, BlockStorage.StorageType.BLOCK_DATA)) {
            String msb = block.getData(msb_nk, BlockStorage.StorageType.BLOCK_DATA);
            String lsb = block.getData(lsb_nk, BlockStorage.StorageType.BLOCK_DATA);

            block.removeData(lsb_nk, BlockStorage.StorageType.BLOCK_DATA);
            block.removeData(msb_nk, BlockStorage.StorageType.BLOCK_DATA);

            UUID uuid = new UUID(Long.getLong(msb), Long.getLong(lsb));

            Entity entity = pedestal.getWorld().getEntity(uuid);

            if(entity != null) {
                entity.remove();
            }
        }
    }

    public static void setNameDisplay(TextComponent text, Location pedestal) {
        ArmorStand stand = (ArmorStand) pedestal.getWorld().spawnEntity(pedestal.clone().add(0.5, 1.2, 0.5), EntityType.ARMOR_STAND);

        stand.setInvisible(true);
        stand.setMarker(true);
        stand.setGravity(false);

        stand.customName(text);
        stand.setCustomNameVisible(true);

        PersistentDataContainer container = stand.getPersistentDataContainer();

        container.set(PEDESTAL, PersistentDataUtil.LOCATION, pedestal);

        StoredBlock block = FlyFunPlugin.get().api.blockStorage().getBlock(pedestal);

        block.setData(DISPLAY_ENTITY_ID_LSB, String.valueOf(stand.getUniqueId().getLeastSignificantBits()), BlockStorage.StorageType.BLOCK_DATA);
        block.setData(DISPLAY_ENTITY_ID_MSB, String.valueOf(stand.getUniqueId().getMostSignificantBits()), BlockStorage.StorageType.BLOCK_DATA);
    }

    public static void removeNameDisplay(Location pedestal) {
        StoredBlock block = FlyFunPlugin.get().api.blockStorage().getBlock(pedestal);

        NamespacedKey lsb_nk = DISPLAY_ENTITY_ID_LSB;
        NamespacedKey msb_nk = DISPLAY_ENTITY_ID_MSB;

        if(block.hasData(msb_nk, BlockStorage.StorageType.BLOCK_DATA) && block.hasData(lsb_nk, BlockStorage.StorageType.BLOCK_DATA)) {
            String msb = block.getData(msb_nk, BlockStorage.StorageType.BLOCK_DATA);
            String lsb = block.getData(lsb_nk, BlockStorage.StorageType.BLOCK_DATA);

            block.removeData(lsb_nk, BlockStorage.StorageType.BLOCK_DATA);
            block.removeData(msb_nk, BlockStorage.StorageType.BLOCK_DATA);

            UUID uuid = new UUID(Long.getLong(msb), Long.getLong(lsb));

            Entity entity = pedestal.getWorld().getEntity(uuid);

            if(entity != null) {
                entity.remove();
            }
        }
    }
}
