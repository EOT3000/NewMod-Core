package me.fly.newmod.flyfun.camera;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import me.fly.newmod.core.util.GeometryUtil;
import me.fly.newmod.flyfun.camera.model.BlockStates;
import me.fly.newmod.flyfun.camera.texture.GetImagePixel;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Camera {
    public short[][] run(Location location) {
        short[][] data = new short[128][128];

        for(int x = 0; x < 128; x++) {
            for(int y = 0; y < 128; y++) {
                Vector vector = GeometryUtil.getRelative(location, new Vector(-0.5+x/128.0, 0, -0.5+y/128.0));

                vector.normalize();

                RayTraceResult result = location.getWorld().rayTraceBlocks(location, vector, 512, FluidCollisionMode.ALWAYS, false);

                BlockStates.BlockState state = Textures.me.getStates(result.getHitBlock().getType()).getState(result.getHitBlock().getBlockData());

                Vector adjusted = GetImagePixel.transform(state.x(), state.y(), result.getHitPosition());

                BlockFace adjustedFace = GetImagePixel.getFace(result.getHitBlockFace(), state.x(), state.y());

                IntIntPair pair = GetImagePixel.getImagePixelFromFaceAndLocation(adjustedFace, adjusted);

                short color = state.model().getMapColor(pair.firstInt(), pair.secondInt(), adjustedFace,
                        null /*No models use this TODO remove it*/, result.getHitBlock().getRelative(result.getHitBlockFace()).getLightLevel());

                data[x][y] = color;
            }
        }

        return data;
    }
}
