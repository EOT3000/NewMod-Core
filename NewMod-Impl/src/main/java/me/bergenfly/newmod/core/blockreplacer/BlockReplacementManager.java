package me.bergenfly.newmod.core.blockreplacer;

import me.bergenfly.newmod.core.blockreplacer.nms.ChunkDataController;

public class BlockReplacementManager {
    public BlockReplacementManager() {
        new ChunkDataController().onEnable();
    }

}
