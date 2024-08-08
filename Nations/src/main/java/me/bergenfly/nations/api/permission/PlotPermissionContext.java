package me.bergenfly.nations.api.permission;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public record PlotPermissionContext(Player user, Block block, Entity entity, Action action, boolean shift) {

}
