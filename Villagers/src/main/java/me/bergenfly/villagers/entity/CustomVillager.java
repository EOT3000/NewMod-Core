package me.bergenfly.villagers.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;

public class CustomVillager extends AbstractNPC {
    public CustomVillager(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
    }
}
