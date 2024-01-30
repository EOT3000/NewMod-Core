package me.fly.newmod.flyfun.camera.texture;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

public interface TexturedBlock {
    short getMapColor(int x, int y, BlockFace face, BlockData data, int brightness);
}
