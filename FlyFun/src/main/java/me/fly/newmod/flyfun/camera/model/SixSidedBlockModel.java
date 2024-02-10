package me.fly.newmod.flyfun.camera.model;

import me.fly.newmod.flyfun.camera.Textures;
import me.fly.newmod.flyfun.camera.texture.TextureData16x16;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

public class SixSidedBlockModel implements BlockModel {
    private final TextureData16x16 north;
    private final TextureData16x16 east;
    private final TextureData16x16 south;
    private final TextureData16x16 west;
    private final TextureData16x16 up;
    private final TextureData16x16 down;

    public SixSidedBlockModel(TextureData16x16 north, TextureData16x16 east, TextureData16x16 south, TextureData16x16 west, TextureData16x16 up, TextureData16x16 down) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        this.up = up;
        this.down = down;
    }

    @Override
    public byte getMapColor(int x, int y, BlockFace face, BlockData data, int brightness) {
        TextureData16x16 use;

        switch (face) {
            case NORTH: use = north; break;
            case SOUTH: use = east; break;
            case EAST: use = south; break;
            case WEST: use = west; break;
            case UP: use = up; break;
            case DOWN: use = down; break;

            default:
                return Textures.FAILED_TO_LOAD.getMapColor(x, y, face, data, brightness);
        }

        return use.storedColor()[brightness * 256 + x * 16 + y];
    }
}
