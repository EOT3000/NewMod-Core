package me.bergenfly.newmod.villagers.sddds;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Villager;

public class CraftAtAnvilBehavior extends VillagerBehavior {
    public static final NamespacedKey KNOWN_ANVILS = new NamespacedKey("temporary", "known_anvils") //TODO: figure out this

    public CraftAtAnvilBehavior() {
        super(null);

        hasExtraStartConditions(this::hasExtraStartConditions).start(this::start);
    }

    private boolean hasExtraStartConditions(World world, Villager villager) {
        //Location[] knownAnvils = villager.getPersistentDataContainer().get(KNOWN_ANVILS, ArrayDataType.)

        Block anvil = villager.getLocation().getBlock().getRelative(BlockFace.NORTH);

        if(!anvil.getType().equals(Material.ANVIL)) {
            anvil = villager.getLocation().getBlock().getRelative(BlockFace.EAST);
        } else if(!anvil.getType().equals(Material.ANVIL)) {
            anvil = villager.getLocation().getBlock().getRelative(BlockFace.SOUTH);
        } else if(!anvil.getType().equals(Material.ANVIL)) {
            anvil = villager.getLocation().getBlock().getRelative(BlockFace.WEST);
        }

        return anvil.getType().equals(Material.ANVIL);
    }

    private void start(World world, Villager villager, long time) {

    }

}
