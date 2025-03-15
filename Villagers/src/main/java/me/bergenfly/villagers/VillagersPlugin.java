package me.bergenfly.villagers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class VillagersPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        super.onEnable();

        Replacer.replace();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Villager villager = null;



        return super.onCommand(sender, command, label, args);
    }
}
