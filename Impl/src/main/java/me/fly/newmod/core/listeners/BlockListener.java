package me.fly.newmod.core.listeners;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.block.BlockManager;
import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.blockstorage.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class BlockListener implements Listener {
    @EventHandler
    public void onServerTick(ServerTickStartEvent event) {
        BlockStorage storage = NewModPlugin.get().blockStorage();
        BlockManager manager = NewModPlugin.get().blockManager();

        for(World world : Bukkit.getWorlds()) {
            for (Location location : storage.getAllStoredLocations(world)) {
                Block b = location.getBlock();
                ModBlock type = manager.getType(b);

                //System.out.println(location + ": " + type);

                if(type == null) {
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
                    storage.removeAllData(location, BlockStorage.StorageType.BLOCK_DATA);

                    NewModPlugin.get().getLogger().warning("Block " + " (" + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getWorld().getName() + ") has been purged for block mismatch");
                }
            }
        }
    }
}
