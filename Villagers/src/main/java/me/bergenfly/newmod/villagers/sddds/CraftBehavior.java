package me.bergenfly.newmod.villagers.sddds;

import org.bukkit.World;
import org.bukkit.entity.Villager;

public class CraftBehavior extends VillagerBehavior {
    public CraftBehavior() {
        super(null);

        hasExtraStartConditions(this::hasExtraStartConditions);
    }

    private boolean hasExtraStartConditions(World world, Villager villager) {
        return world.block
    }

}
