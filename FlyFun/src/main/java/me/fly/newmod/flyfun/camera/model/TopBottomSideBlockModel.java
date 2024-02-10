package me.fly.newmod.flyfun.camera.model;

import me.fly.newmod.flyfun.camera.Textures;
import me.fly.newmod.flyfun.camera.texture.TextureData16x16;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

public class TopBottomSideBlockModel implements BlockModel {
    private final TextureData16x16 top;
    private final TextureData16x16 bottom;
    private final TextureData16x16 side;

    public TopBottomSideBlockModel(TextureData16x16 top, TextureData16x16 bottom, TextureData16x16 side) {
        this.top = top;
        this.bottom = bottom;
        this.side = side;
    }

    @Override
    public byte getMapColor(int x, int y, BlockFace face, BlockData data, int brightness) {
        TextureData16x16 use;

        switch (face) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
                use = side;
                break;
            case UP:
                use = top;
                break;
            case DOWN:
                use = bottom;
                break;
            default:
                return Textures.FAILED_TO_LOAD.getMapColor(x, y, face, data, brightness);
        }

        return use.storedColor()[brightness * 256 + x * 16 + y];
    }
}
