package me.bergenfly.newmod.flyfun.camera.model;

import me.bergenfly.newmod.flyfun.camera.Textures;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import java.util.List;
import java.util.Set;

public class UnknownModel implements BlockModel {
    public String parent;
    public Set<String> textures;

    public UnknownModel(String parent, Set<String> textures) {
        this.parent = parent;
        this.textures = textures;
    }

    @Override
    public byte getMapColor(int x, int y, BlockFace face, BlockData data, int brightness) {
        return Textures.FAILED_TO_LOAD.getMapColor(x, y, face, data, brightness);
    }

    @Override
    public String texturesString() {
        return textures.toString();
    }
}
