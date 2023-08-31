package me.fly.newmod.flyfun.magic.block.spawner;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.util.PersistentDataUtil;
import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.magic.MagicTypes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collections;
import java.util.List;

public class RepairedSpawner implements ModBlock {
    public static final NamespacedKey ENTITY_TYPE = new NamespacedKey(FlyFunPlugin.get(), "entity_type");

    private final NamespacedKey id;
    private final Material material;

    public RepairedSpawner() {
        this.id = new NamespacedKey(FlyFunPlugin.get(), "repaired_spawner");
        this.material = Material.DISPENSER;
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
    public void place(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        String e = item.getItemMeta().getPersistentDataContainer().get(ENTITY_TYPE, PersistentDataType.STRING);

        if(e != null) {
            EntityType entity = EntityType.valueOf(e);
            CreatureSpawner blockState = (CreatureSpawner) event.getBlock().getState();

            blockState.setSpawnedType(entity);

            blockState.update();
        }

    }

    @Override
    public void place0(Block block) {
        CreatureSpawner blockState = (CreatureSpawner) block.getState();

        blockState.setSpawnedType(EntityType.PIG);

        blockState.update();
    }

    @Override
    public List<ItemStack> getDrops(Block block, Player breaker) {
        return Collections.singletonList(MagicTypes.REPAIRED_SPAWNER.create());
    }

    public static ItemStack itemOfType(EntityType type) {
        ItemStack stack = MagicTypes.REPAIRED_SPAWNER.create();

        PersistentDataUtil.setString(stack, ENTITY_TYPE, type.toString());

        return stack;
    }
}
