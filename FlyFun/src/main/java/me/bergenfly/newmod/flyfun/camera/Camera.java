package me.bergenfly.newmod.flyfun.camera;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import me.bergenfly.newmod.core.util.GeometryUtil;
import me.bergenfly.newmod.flyfun.camera.model.BlockStates;
import me.bergenfly.newmod.flyfun.camera.texture.GetImagePixel;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.Random;

public class Camera {
    private static int asdj = 0;
    private static Random djkjfk = new Random();

    public static byte[][] run(Location location) {
        byte[][] data = new byte[128][128];

        long date = System.currentTimeMillis();

        for(int x = 0; x < 128; x+=1) {
            for(int y = 0; y < 128; y+=1) {
                Vector vector = GeometryUtil.getRelative(location, new Vector(0.5-x/128.0, 0.5-y/128.0, 1)).subtract(location.toVector());

                RayTraceResult result = location.getWorld().rayTraceBlocks(location, vector, 512, FluidCollisionMode.ALWAYS, false);

                if(result != null) {

                    //System.out.println("ray " + x + "," + y + "hit!");
                    //System.out.println(result);

                    boolean p = false;

                    if(asdj++ % 2 == 0) {
                        if(djkjfk.nextInt(10) == 0 && result.getHitBlock().getType().equals(Material.HAY_BLOCK)) {
                            System.out.println("Found a hay bale at " + result.getHitBlock().getLocation() + " and its at face " + result.getHitBlockFace());
                            System.out.println("It is at axis " + ((Orientable) result.getHitBlock().getBlockData()).getAxis());
                            System.out.println("Hit position: " + result.getHitPosition().subtract(new Vector(result.getHitPosition().getBlockX(), result.getHitPosition().getBlockY(), result.getHitPosition().getBlockZ())));
                            System.out.println("Hit position: " + result.getHitPosition());
                            System.out.println("Would be pixels: " + GetImagePixel.getImagePixelFromFaceAndLocation(result.getHitBlockFace(), result.getHitPosition(), true));
                            p = true;
                        }
                    }

                    if(x == 64 && y == 64) {
                        BlockDisplay display = location.getWorld().spawn(result.getHitPosition().toLocation(location.getWorld()), BlockDisplay.class);

                        display.setBlock(Material.STONE.createBlockData());
                        display.setTransformation(new Transformation(new Vector3f(0, 0, 0), new AxisAngle4f(), new Vector3f(0.05f, 0.05f, 0.05f), new AxisAngle4f()));
                    }

                    BlockStates.BlockState state = Textures.me.getStates(result.getHitBlock().getType()).getState(result.getHitBlock().getBlockData());

                    Vector adjusted = GetImagePixel.transform(state.x(), state.y(), result.getHitPosition().clone().subtract(result.getHitBlock().getLocation().toVector())
                            .subtract(new Vector(0.5,0.5,0.5)), p).add(new Vector(0.5,0.5,0.5));

                    //

                    if(x == 64 && y == 64) {
                        BlockDisplay display2 = location.getWorld().spawn(adjusted.toLocation(location.getWorld()).add(result.getHitBlock().getLocation().toVector()), BlockDisplay.class);

                        display2.setBlock(Material.GOLD_BLOCK.createBlockData());
                        display2.setTransformation(new Transformation(new Vector3f(0, 0, 0), new AxisAngle4f(), new Vector3f(0.05f, 0.05f, 0.05f), new AxisAngle4f()));
                    }

                    BlockFace adjustedFace = GetImagePixel.getFace(result.getHitBlockFace(), state.x(), state.y(), p);

                    IntIntPair pair = GetImagePixel.getImagePixelFromFaceAndLocation(adjustedFace, adjusted, p);
                    //IntIntPair pair = GetImagePixel.getImagePixelFromFaceAndLocation(result.getHitBlockFace(), result.getHitPosition(), p);

                    //System.out.println("adjusted coordinate: " + pair.firstInt() + "," + pair.secondInt());
                    //System.out.println("adjusted face: " + adjustedFace);

                    byte color = state.model().getMapColor(pair.firstInt(), pair.secondInt(), adjustedFace,
                            null /*No models use this TODO remove it*/, result.getHitBlock().getRelative(result.getHitBlockFace()).getLightLevel());
                    data[x][y] = color;

                    if(p) {
                        System.out.println();
                    }
                } else {

                }

                if(data[x][y] == 0) {
                    data[x][y] = MapPalette.PALE_BLUE;
                }
            }

            //System.out.println("spent " + (System.currentTimeMillis()-date) + " on layer " + x);
            //date = System.currentTimeMillis();
        }

        System.out.println("spent " + (System.currentTimeMillis()-date));

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
