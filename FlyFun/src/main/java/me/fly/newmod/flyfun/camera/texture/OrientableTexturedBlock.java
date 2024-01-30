package me.fly.newmod.flyfun.camera.texture;

import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;

public class OrientableTexturedBlock implements TexturedBlock {
    private final TextureData16x16 end;
    private final TextureData16x16 side;

    public OrientableTexturedBlock(TextureData16x16 end, TextureData16x16 side) {
        this.end = end;
        this.side = side;
    }

    @Override
    public short getMapColor(int x, int y, BlockFace face, BlockData data, int brightness) {
        if(data instanceof Orientable orientable) {
            TextureData16x16 use;
            switch (face) {
                case NORTH:
                case SOUTH: use = orientable.getAxis().equals(Axis.Z) ? end : side; break;
                case EAST:
                case WEST: use = orientable.getAxis().equals(Axis.X) ? end : side; break;
                case UP:
                case DOWN: use = orientable.getAxis().equals(Axis.Y) ? end : side; break;
                default: return Textures.FAILED_TO_LOAD.getMapColor(x, y, face, data, brightness);
            }

            return (short) (use.storedColor()[brightness*256+x*16+y]+128);
        }

        return Textures.FAILED_TO_LOAD.getMapColor(x, y, face, data, brightness);
    }
}
