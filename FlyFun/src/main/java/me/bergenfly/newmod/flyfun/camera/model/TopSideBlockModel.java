package me.bergenfly.newmod.flyfun.camera.model;

import me.bergenfly.newmod.core.util.ColorUtil;
import me.bergenfly.newmod.flyfun.camera.texture.TextureData16x16;
import me.bergenfly.newmod.flyfun.camera.Textures;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;

public class TopSideBlockModel implements BlockModel {
    private final TextureData16x16 end;
    private final TextureData16x16 side;
    private final boolean horizontal;

    public TopSideBlockModel(TextureData16x16 end, TextureData16x16 side, boolean horizontal) {
        this.end = end;
        this.side = side;
        this.horizontal = horizontal;
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
                if(horizontal) {
                    return end.storedColor()[brightness * 256 + (15-x) * 16 + (15-y)];
                }
            case DOWN:
                use = end;
                break;
            default:
                return Textures.FAILED_TO_LOAD.getMapColor(x, y, face, data, brightness);
        }

        return use.storedColor()[brightness * 256 + x * 16 + y];
    }

    @Override
    public int getColor(int x, int y, BlockFace face, BlockData data) {
        TextureData16x16 use;

        switch (face) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
                use = side;
                break;
            case UP:
                if(horizontal) {
                    return end.rawColor()[(15-x) * 16 + (15-y)];
                }
            case DOWN:
                use = end;
                break;
            default:
                return Textures.FAILED_TO_LOAD.getMapColor(x, y, face, data, 15);
        }

        return use.rawColor()[x*16+y];
    }

    @Override
    public String texturesString() {
        return
                "e: " + end.id() +
                        ";s: " + side.id();
    }
}
