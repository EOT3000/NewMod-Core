package me.fly.newmod.flyfun.camera.model;

import me.fly.newmod.flyfun.camera.texture.TextureData16x16;
import me.fly.newmod.flyfun.camera.Textures;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;

public class TopSideBlockModel implements BlockModel {
    private final TextureData16x16 end;
    private final TextureData16x16 side;

    public TopSideBlockModel(TextureData16x16 end, TextureData16x16 side) {
        this.end = end;
        this.side = side;
    }

    @Override
    public byte getMapColor(int x, int y, BlockFace face, BlockData data, int brightness) {
        TextureData16x16 use;

        System.out.println(face);

        switch (face) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
                use = side;
                break;
            case UP:
            case DOWN:
                use = end;
                break;
            default:
                System.out.println("defaulted");
                return Textures.FAILED_TO_LOAD.getMapColor(x, y, face, data, brightness);
        }

        return use.storedColor()[brightness * 256 + x * 16 + y];
    }
}
