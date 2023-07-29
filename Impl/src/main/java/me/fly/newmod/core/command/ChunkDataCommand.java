package me.fly.newmod.core.command;

import com.jeff_media.customblockdata.CustomBlockData;
import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.blockstorage.BlockStorage;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ChunkDataCommand {
    private static final BlockStorage storage = NewModPlugin.get().blockStorage();

    public void run(Player player) {
        Chunk c = player.getChunk();

        Set<Block> s = CustomBlockData.getBlocksWithCustomData(NewModPlugin.get(), c);

        player.sendMessage("The chunk you are in (" + c.getX() + ", " + c.getZ() + ", " + c.getWorld().getName() + ") has " + s.size() + " blocks");

        Set<Block> i = s.stream().sorted(Comparator.comparingInt(o -> o.getLocation().getBlockY())).collect(Collectors.toCollection(LinkedHashSet::new));

        //TODO: while this is fine for now, make this handle bigger numbers later

        //TODO: finish this

        String toSend = "";

        for(Block b : i) {
            if(Math.abs(b.getY()-player.getLocation().getBlockY()) <= 16) {
                toSend += "";
            }
        }
    }
}
