package me.bergenfly.newmod.flyfun.camera.model;

import me.bergenfly.newmod.core.util.MapColor;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

//Model has a parent, then defined textures. For example, 6 sided blocks have the same texture for each
public interface BlockModel {
    byte getMapColor(int x, int y, BlockFace face, BlockData data, int brightness);

    default int getColor(int x, int y, BlockFace face, BlockData data) {
        int b = Byte.toUnsignedInt(getMapColor(x, y, face, data, 15));

        return MapColor.values()[b/4].variation(b%4);
    }

    default String texturesString() {
        return "Custom";
    }
}
