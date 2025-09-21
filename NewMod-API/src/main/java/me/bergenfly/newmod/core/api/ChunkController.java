package me.bergenfly.newmod.core.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

//Todo make this nice and API like
public interface ChunkController {
    void sendPhantomBlock(Location location, Player player, Material material, String data);
}
