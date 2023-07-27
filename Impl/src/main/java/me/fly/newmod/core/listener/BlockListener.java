package me.fly.newmod.core.listener;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import com.jeff_media.customblockdata.CustomBlockData;
import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.block.BlockManager;
import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.item.ModItem;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    @EventHandler
    public void onServerTick(ServerTickStartEvent event) {
        BlockManager manager = NewModPlugin.get().blockManager();

        for(World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Block b : CustomBlockData.getBlocksWithCustomData(NewModPlugin.get(), chunk)) {
                    ModBlock type = manager.getType(b);

                    Location location = b.getLocation();

                    //System.out.println(location + ": " + type);

                    if (type == null) {
                        continue;
                    }

                    if (type.getMaterial().equals(b.getType())) {
                        try {
                            type.tick(event.getTickNumber(), b, manager.from(b));
                        } catch (Exception e) {
                            NewModPlugin.get().getLogger().warning("Block " + " (" + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getWorld().getName() + ") error:");

                            e.printStackTrace();
                        }
                    } else {
                        NewModPlugin.get().getLogger().warning("Block " + " (" + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getWorld().getName() + ") has been purged for block mismatch");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ModItem type = NewModPlugin.get().itemManager().getType(event.getItemInHand());

        if(type != null) {
            if(type.getBlock() != null) {
                type.getBlock().place(event.getBlock(), null);
            }
        }
    }
}
