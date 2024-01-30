package me.fly.newmod.flyfun.camera.texture;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

public class SixSidedTexturedBlock implements TexturedBlock {
    private final TextureData16x16 data;

    public SixSidedTexturedBlock(TextureData16x16 data) {
        this.data = data;
    }

    @Override
    public short getMapColor(int x, int y, BlockFace face, BlockData data, int brightness) {
        return (short) (this.data.storedColor()[brightness*256+x*16+y]+128);
    }
}
