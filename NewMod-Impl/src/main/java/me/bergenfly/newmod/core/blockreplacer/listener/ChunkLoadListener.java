package me.bergenfly.newmod.core.blockreplacer.listener;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import me.bergenfly.newmod.core.NewModPlugin;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.blockstorage.StoredBlock;
import me.bergenfly.newmod.core.blockreplacer.nms.ChunkDataController;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoadListener implements Listener {

    public static final NamespacedKey DEAD_DISPLAY_MATERIAL = new NamespacedKey("newmod-core", "dead_display_material");
    public static final NamespacedKey DEAD_DISPLAY_DATA = new NamespacedKey("newmod-core", "dead_display_age");

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkLoad(PlayerChunkLoadEvent event) {
        for(Location location : NewModPlugin.get().blockStorage().getAllStoredLocations(event.getChunk())) {
            StoredBlock block = NewModPlugin.get().blockStorage().getBlock(location);

            String material = block.getData(DEAD_DISPLAY_MATERIAL, BlockStorage.StorageType.BLOCK_DATA);
            String data = block.getData(DEAD_DISPLAY_DATA, BlockStorage.StorageType.BLOCK_DATA);

            if((material != null) && (data != null)) {
                if(location.getBlock().getType().equals(Material.AIR)) {
                    block.removeData(DEAD_DISPLAY_MATERIAL, BlockStorage.StorageType.BLOCK_DATA);
                    block.removeData(DEAD_DISPLAY_DATA, BlockStorage.StorageType.BLOCK_DATA);
                    continue;
                }

                Bukkit.getScheduler().runTaskLater(NewModPlugin.get(), () -> {
                    NewModPlugin.get().blockReplacementManager.c.sendPhantomBlock(
                            location,
                            event.getPlayer(),
                            Material.getMaterial(material),
                            data);
                }, 1);
            }
        }
    }
}
