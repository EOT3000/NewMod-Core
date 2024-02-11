package me.fly.newmod.flyfun.camera.texture;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class GetImagePixel {
    //kms
    private static final BlockFace[][][] transformed = new BlockFace[6][4][4];

    static {
        for(BlockFace face : BlockFace.values()) {
            if(face == BlockFace.NORTH_EAST) {
                break;
            }

            System.out.println("Beginning " + face.name());

            for(int x = 0; x < 4; x++) {
                for(int y = 0; y < 4; y++) {
                    System.out.println("Rotating back " + x + " x and " + y + " y");

                    int mx = face.getModX();
                    int my = face.getModY();
                    int mz = face.getModZ();

                    for(int xx = 0; xx < x; xx++) {
                        int ny = mz;
                        int nz = -my;

                        my = ny;
                        mz = nz;

                        System.out.println(xx + " time x: " + getFace(mx, my, mz));
                    }

                    for(int yy = 0; yy < y; yy++) {
                        int nx = mz;
                        int nz = -mx;

                        mx = nx;
                        mz = nz;

                        System.out.println(yy + " time y: " + getFace(mx, my, mz));
                    }

                    //Reverse order of x and y?

                    System.out.println();

                    transformed[face.ordinal()][x][y] = getFace(mx, my, mz);
                }
            }
        }

        System.out.println(Arrays.deepToString(transformed));
    }

    public static BlockFace getFace(int x, int y, int z) {
        for(BlockFace face : BlockFace.values()) {
            if(face.getModX() == x && face.getModY() == y && face.getModZ() == z) {
                return face;
            }
        }

        return null;
    }

    public static IntIntPair getImagePixelFromFaceAndLocation(BlockFace face, Vector location) {
        int x1 = -1;
        int y1 = -1;

        switch (face) {
            case UP -> {
                x1 = (int) ((location.getX()-location.getBlockX())*16.0);
                y1 = (int) ((location.getZ()-location.getBlockZ())*16.0);
            }
            case DOWN -> {
                x1 = (int) ((location.getX()-location.getBlockX())*16.0);
                y1 = 15-(int) ((location.getZ()-location.getBlockZ())*16.0);
            }
            case NORTH -> {
                x1 = 15-(int) ((location.getX()-location.getBlockX())*16.0);
                y1 = 15-(int) ((location.getY()-location.getBlockY())*16.0);
            }
            case EAST -> {
                x1 = 15-(int) ((location.getZ()-location.getBlockZ())*16.0);
                y1 = 15-(int) ((location.getY()-location.getBlockY())*16.0);
            }
            case SOUTH -> {
                x1 = (int) ((location.getX()-location.getBlockX())*16.0);
                y1 = 15-(int) ((location.getY()-location.getBlockY())*16.0);
            }
            case WEST -> {
                x1 = (int) ((location.getZ()-location.getBlockZ())*16.0);
                y1 = 15-(int) ((location.getY()-location.getBlockY())*16.0);
            }
        }

        if(x1 == -1 || y1 == -1) {
            return null;
        }

        return new IntIntImmutablePair(x1, y1);
    }

    public static Vector transform(int x, int y, Vector location) {
        Vector vector = location.clone();

        vector.rotateAroundX(Math.toRadians(-x));
        vector.rotateAroundY(Math.toRadians(-y));

        return vector;
    }

    public static BlockFace getFace(BlockFace original, int x, int y) {
        return transformed[original.ordinal()][x/90][y/90];
    }
}
