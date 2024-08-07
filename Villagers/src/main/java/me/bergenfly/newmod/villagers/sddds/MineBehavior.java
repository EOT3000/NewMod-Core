package me.bergenfly.newmod.villagers.sddds;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.EntityVillager;

import java.util.Map;

public class MineBehavior extends VillagerBehavior {
    public MineBehavior(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryState) {
        super(null);
    }
}
