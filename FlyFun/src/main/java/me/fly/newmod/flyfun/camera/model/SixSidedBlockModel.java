package me.fly.newmod.flyfun.camera.model;

import me.fly.newmod.flyfun.camera.texture.TextureData16x16;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

public class SixSidedBlockModel implements BlockModel {
    private final TextureData16x16 data;

    public SixSidedBlockModel(TextureData16x16 data) {
        this.data = data;
    }

    @Override
    public byte getMapColor(int x, int y, BlockFace face, BlockData data, int brightness) {
        return this.data.storedColor()[brightness*256+x*16+y];
    }
}
