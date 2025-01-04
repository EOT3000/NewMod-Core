package me.bergenfly.newmod.flyfun.camera;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import me.bergenfly.newmod.core.util.ColorUtil;
import me.bergenfly.newmod.core.util.GeometryUtil;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.camera.model.BlockStates;
import me.bergenfly.newmod.flyfun.camera.texture.GetImagePixel;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
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

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Camera {
    private static int asdj = 0;
    private static Random djkjfk = new Random();

    private static AtomicInteger number = new AtomicInteger();

    public static void loadFile(File file, Player player) {
        Int2ObjectArrayMap<BlockData> palette = new Int2ObjectArrayMap<>();

        int[][] colors = new int[256][256];

        try {
            Scanner scanner = new Scanner(file);

            long time = Long.parseLong(scanner.next());
            int count = Integer.parseInt(scanner.next());

            for (int i = 0; i < count; i++) {
                String line = scanner.nextLine();

                String[] spl = line.split(":");

                palette.put(Integer.parseInt(spl[0]), Bukkit.createBlockData(spl[1]));
            }

            int c = 0;

            while (scanner.hasNext()) {
                int xM = (int) (c/256.0);
                int yM = c-xM*256;

                c++;

                String line = scanner.nextLine();

                if(!line.equalsIgnoreCase("null")) {
                    String[] spl = line.split(":");

                    BlockData blockData = palette.get(Integer.parseInt(spl[0]));
                    double x = Integer.parseInt(spl[1]) / 1000.0;
                    double y = Integer.parseInt(spl[2]) / 1000.0;
                    double z = Integer.parseInt(spl[3]) / 1000.0;
                    int faceInt = Integer.parseInt(spl[4]);
                    BlockFace face = BlockFace.values()[faceInt];
                    int blockB = Integer.parseInt(spl[5]);
                    int skyB = Integer.parseInt(spl[6]);
                    int totB = Integer.parseInt(spl[7]);

                    BlockStates.BlockState state = Textures.me.getStates(blockData.getMaterial()).getState(blockData);

                    Vector adjusted = GetImagePixel.transform(state.x(), state.y(), new Vector(x,y,z)
                            .subtract(new Vector(0.5, 0.5, 0.5)), false).add(new Vector(0.5, 0.5, 0.5));

                    BlockFace adjustedFace = GetImagePixel.getFace(face, state.x(), state.y(), false);

                    IntIntPair pair = GetImagePixel.getImagePixelFromFaceAndLocation(adjustedFace, adjusted, false);

                    int color = state.model().getColor(pair.firstInt(), pair.secondInt(), adjustedFace,
                            null, totB);
                    colors[xM][yM] = color;
                }
            }

            byte[][] data = new byte[128][128];

            for(int x = 0; x < 128; x++) {
                for(int y = 0; y < 128; y++) {
                    double[] Lab1 = ColorUtil.rgbToOklab(colors[x+0][y+0]);
                    double[] Lab2 = ColorUtil.rgbToOklab(colors[x+1][y+0]);
                    double[] Lab3 = ColorUtil.rgbToOklab(colors[x+0][y+1]);
                    double[] Lab4 = ColorUtil.rgbToOklab(colors[x+1][y+1]);

                    byte close = ColorUtil.findClosestColor(
                            (Lab1[0]+Lab2[0]+Lab3[0]+Lab4[0])/4.0,
                            (Lab1[1]+Lab2[1]+Lab3[1]+Lab4[1])/4.0,
                            (Lab1[2]+Lab2[2]+Lab3[2]+Lab4[2])/4.0
                    );

                    data[x][y] = close;
                    FlyFunPlugin.get().giveToPlayer(data, player);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(Location location) {
        ResultMoreData[] results = capture(location);

        Thread nt = new Thread(() -> {
            long start = System.currentTimeMillis();

            Object2IntMap<String> palette = new Object2IntOpenHashMap<>();
            List<String> data = new ArrayList<>(256*256);
            int count = 0;

            long time = location.getWorld().getTime();

            for(ResultMoreData resultM : results) {
                RayTraceResult result = resultM.result;

                if(result == null || result.getHitBlock() == null) {
                    data.add("null");
                    continue;
                }

                String type = result.getHitBlock().getBlockData().getAsString();

                Vector relative = result.getHitPosition().subtract(result.getHitBlock().getLocation().toVector());

                int x = (int) Math.round(relative.getX()*1000);
                int y = (int) Math.round(relative.getX()*1000);
                int z = (int) Math.round(relative.getX()*1000);

                String pos = x + ":" + y + ":" + z;

                if(palette.containsKey(type)) {
                    data.add(palette.getInt(type) + ":" + pos + ":" + result.getHitBlockFace().ordinal() + ":" + resultM.blockBrightness + ":" + resultM.skyBrightness);
                } else {
                    count++;
                    palette.put(type, count);
                    data.add(count + ":" + pos + ":" + result.getHitBlockFace().ordinal() + ":" + resultM.blockBrightness + ":" + resultM.skyBrightness + ":" + resultM.brightness);
                }
            }

            try {
                File file = new File("photo" + number.getAndIncrement());

                file.createNewFile();

                try(FileOutputStream stream = new FileOutputStream(file)) {
                    PrintWriter writer = new PrintWriter(stream);

                    writer.write(Objects.toString(time));
                    writer.write('\n');

                    writer.write(Objects.toString(count));
                    writer.write('\n');

                    for(Object2IntMap.Entry<String> type : palette.object2IntEntrySet()) {
                        writer.write(type.getIntValue() + ":" + type.getKey());
                        writer.write('\n');
                    }

                    for(String dat : data) {
                        writer.write(dat);
                        writer.write("\n");
                    }
                }
            } catch (Exception e) {
                // ):
            }

            System.out.println("Took " + (System.currentTimeMillis()-start) + " millis to write to file");
        });

        nt.start();
    }

    public static ResultMoreData[] capture(Location location) {
        //byte[][] data = new byte[128][128];

        long date = System.currentTimeMillis();

        ResultMoreData[] ret = new ResultMoreData[256*256];

        for(int x = 0; x < 256; x+=1) {
            for(int y = 0; y < 256; y+=1) {
                Vector vector = GeometryUtil.getRelative(location, new Vector(0.5-x/256.0, 0.5-y/256.0, 1)).subtract(location.toVector());

                RayTraceResult result = location.getWorld().rayTraceBlocks(location, vector, 512, FluidCollisionMode.ALWAYS, false);

                if(result != null && result.getHitBlock() != null) {
                    Block nb = result.getHitBlock().getLocation().add(result.getHitBlockFace().getDirection()).getBlock();

                    ret[x * 256 + y] = new ResultMoreData(result, nb.getLightFromBlocks(), nb.getLightFromSky(), nb.getLightLevel());
                } else {
                    ret[x * 256 + y] = new ResultMoreData(result, 0, 0, 0);
                }

                //if(result != null) {

                    //System.out.println("ray " + x + "," + y + "hit!");
                    //System.out.println(result);

                    //boolean p = false;

                    /*if(asdj++ % 2 == 0) {
                        if(djkjfk.nextInt(10) == 0 && result.getHitBlock().getType().equals(Material.HAY_BLOCK)) {
                            System.out.println("Found a hay bale at " + result.getHitBlock().getLocation() + " and its at face " + result.getHitBlockFace());
                            System.out.println("It is at axis " + ((Orientable) result.getHitBlock().getBlockData()).getAxis());
                            System.out.println("Hit position: " + result.getHitPosition().subtract(new Vector(result.getHitPosition().getBlockX(), result.getHitPosition().getBlockY(), result.getHitPosition().getBlockZ())));
                            System.out.println("Hit position: " + result.getHitPosition());
                            System.out.println("Would be pixels: " + GetImagePixel.getImagePixelFromFaceAndLocation(result.getHitBlockFace(), result.getHitPosition(), true));
                            p = true;
                        }
                    }*/

                    /*if(x == 64 && y == 64) {
                        BlockDisplay display = location.getWorld().spawn(result.getHitPosition().toLocation(location.getWorld()), BlockDisplay.class);

                        display.setBlock(Material.STONE.createBlockData());
                        display.setTransformation(new Transformation(new Vector3f(0, 0, 0), new AxisAngle4f(), new Vector3f(0.05f, 0.05f, 0.05f), new AxisAngle4f()));
                    }

                    BlockStates.BlockState state = Textures.me.getStates(result.getHitBlock().getType()).getState(result.getHitBlock().getBlockData());

                    Vector adjusted = GetImagePixel.transform(state.x(), state.y(), result.getHitPosition().clone().subtract(result.getHitBlock().getLocation().toVector())
                            .subtract(new Vector(0.5,0.5,0.5)), p).add(new Vector(0.5,0.5,0.5));*/

                    //

                    /*if(x == 64 && y == 64) {
                        BlockDisplay display2 = location.getWorld().spawn(adjusted.toLocation(location.getWorld()).add(result.getHitBlock().getLocation().toVector()), BlockDisplay.class);

                        display2.setBlock(Material.GOLD_BLOCK.createBlockData());
                        display2.setTransformation(new Transformation(new Vector3f(0, 0, 0), new AxisAngle4f(), new Vector3f(0.05f, 0.05f, 0.05f), new AxisAngle4f()));
                    }*/

                    /*BlockFace adjustedFace = GetImagePixel.getFace(result.getHitBlockFace(), state.x(), state.y(), p);

                    IntIntPair pair = GetImagePixel.getImagePixelFromFaceAndLocation(adjustedFace, adjusted, p);
                    //IntIntPair pair = GetImagePixel.getImagePixelFromFaceAndLocation(result.getHitBlockFace(), result.getHitPosition(), p);

                    //System.out.println("adjusted coordinate: " + pair.firstInt() + "," + pair.secondInt());
                    //System.out.println("adjusted face: " + adjustedFace);*/

                    /*byte color = state.model().getMapColor(pair.firstInt(), pair.secondInt(), adjustedFace,
                            null /*No models use this TODO remove it, result.getHitBlock().getRelative(result.getHitBlockFace()).getLightLevel());
                    data[x][y] = color;

                    if(p) {
                        System.out.println();
                    }*/
                //}

                /*if(data[x][y] == 0) {
                    data[x][y] = MapPalette.PALE_BLUE;
                }*/
            }

            //System.out.println("spent " + (System.currentTimeMillis()-date) + " on layer " + x);
            //date = System.currentTimeMillis();
        }

        System.out.println("spent " + (System.currentTimeMillis()-date));

        return ret;
    }

    public record ResultMoreData(RayTraceResult result, int blockBrightness, int skyBrightness, int brightness) {}

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
