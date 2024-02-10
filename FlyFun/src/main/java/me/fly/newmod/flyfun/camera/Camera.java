package me.fly.newmod.flyfun.camera;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import me.fly.newmod.core.util.GeometryUtil;
import me.fly.newmod.flyfun.camera.model.BlockStates;
import me.fly.newmod.flyfun.camera.texture.GetImagePixel;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Camera {
    public static byte[][] run(Location location) {
        byte[][] data = new byte[128][128];

        long date = System.currentTimeMillis();

        for(int x = 0; x < 128; x++) {
            for(int y = 0; y < 128; y++) {
                Vector vector = GeometryUtil.getRelative(location, new Vector(-0.5+x/128.0, 0, -0.5+y/128.0));

                vector.normalize();

                RayTraceResult result = location.getWorld().rayTraceBlocks(location, vector, 512, FluidCollisionMode.ALWAYS, false);

                if(result != null) {
                    BlockStates.BlockState state = Textures.me.getStates(result.getHitBlock().getType()).getState(result.getHitBlock().getBlockData());

                    Vector adjusted = GetImagePixel.transform(state.x(), state.y(), result.getHitPosition());

                    BlockFace adjustedFace = GetImagePixel.getFace(result.getHitBlockFace(), state.x(), state.y());

                    IntIntPair pair = GetImagePixel.getImagePixelFromFaceAndLocation(adjustedFace, adjusted);

                    byte color = state.model().getMapColor(pair.firstInt(), pair.secondInt(), adjustedFace,
                            null /*No models use this TODO remove it*/, result.getHitBlock().getRelative(result.getHitBlockFace()).getLightLevel());
                    data[x][y] = color;
                }
            }

            System.out.println("spent " + (System.currentTimeMillis()-date) + " on layer " + x);
            date = System.currentTimeMillis();
        }

        return data;
    }

    public static final class Renderer extends MapRenderer {
        private final byte[][] data;

        public Renderer(byte[][] data) {
            this.data = data;
        }

        @Override
        public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
            for(int x = 0; x < 128; x++) {
                for(int y = 0; y < 128; y++) {
                    mapCanvas.setPixel(x, y, data[x][y]);
                }
            }
        }

        @Override
        public boolean isExplorerMap() {
            return false;
        }
    }
}
