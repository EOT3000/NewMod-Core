package me.bergenfly.newmod.flyfun.camera.model;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

public interface BlockModel {
    byte getMapColor(int x, int y, BlockFace face, BlockData data, int brightness);
}
