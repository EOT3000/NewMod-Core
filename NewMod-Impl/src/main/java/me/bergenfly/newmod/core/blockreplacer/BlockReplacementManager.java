package me.bergenfly.newmod.core.blockreplacer;

import me.bergenfly.newmod.core.blockreplacer.nms.ChunkDataController;

public class BlockReplacementManager {
    public ChunkDataController c;

    public BlockReplacementManager() {
        c = new ChunkDataController();

        c.onEnable();
    }

}
