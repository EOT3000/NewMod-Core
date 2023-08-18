package me.fly.newmod.villagers.task;

import me.fly.newmod.villagers.VillagerData;
import org.bukkit.entity.Villager;

public interface BaseTask {
    void run(Villager villager, VillagerData data);
}
