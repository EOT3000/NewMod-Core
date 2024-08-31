package me.bergenfly.newmod.flyfun.camera.model;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

//Model has a parent, then defined textures. For example, 6 sided blocks have the same texture for each
public interface BlockModel {
    byte getMapColor(int x, int y, BlockFace face, BlockData data, int brightness);
}
