package me.bergenfly.newmod.core.blockreplacer.listener;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import me.bergenfly.newmod.core.NewModPlugin;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.blockstorage.StoredBlock;
import me.bergenfly.newmod.core.blockreplacer.nms.ChunkDataController;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoadListener implements Listener {

    public static final NamespacedKey DEAD_CACTUS = new NamespacedKey(NewModPlugin.get(), "dead_cactus");

    @EventHandler
    public void onChunkLoad(PlayerChunkLoadEvent event) {
        for(Location location : NewModPlugin.get().blockStorage().getAllStoredLocations(event.getChunk())) {
            StoredBlock block = NewModPlugin.get().blockStorage().getBlock(location);

            if(block.hasData(DEAD_CACTUS, BlockStorage.StorageType.BLOCK_DATA)) {
                // event is supposedly not async so this works
                NewModPlugin.get().blockReplacementManager.c.sendCactus(location, event.getPlayer());
            }
        }
    }
}
