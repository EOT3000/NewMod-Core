package me.fly.newmod.flyfun.camera.texture;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class GetImagePixelFromRotationAndLocation {
    //kms
    private static final BlockFace[][][] transformed = new BlockFace[6][4][4];

    static {
        for(BlockFace face : BlockFace.values()) {
            if(face == BlockFace.NORTH_EAST) {
                break;
            }

            for(int x = 0; x < 4; x++) {
                for(int y = 0; y < 4; y++) {
                    int mx = face.getModX();
                    int my = face.getModY();
                    int mz = face.getModZ();

                    for(int xx = 0; xx < x; xx++) {
                        int ny = mz;
                        int nz = -my;

                        my = ny;
                        mz = nz;
                    }

                    for(int yy = 0; yy < x; yy++) {
                        int nx = mz;
                        int nz = -mx;

                        mx = nx;
                        mz = nz;
                    }

                    transformed[face.ordinal()][x][y] = getFace(mx, my, mz);
                }
            }
        }
    }

    public static BlockFace getFace(int x, int y, int z) {
        for(BlockFace face : BlockFace.values()) {
            if(face.getModX() == x && face.getModY() == y && face.getModZ() == z) {
                return face;
            }
        }

        return null;
    }

    public static IntIntPair getImagePixelFromRotationAndLocation(BlockFace face, Location location) {
        int x1 = -1;
        int y1 = -1;

        switch (face) {
            case UP -> {
                x1 = (int) ((location.getBlockX()-location.getX())*16.0);
                y1 = (int) ((location.getBlockZ()-location.getZ())*16.0);
            }
            case DOWN -> {
                x1 = (int) ((location.getBlockX()-location.getX())*16.0);
                y1 = 16-(int) ((location.getBlockZ()-location.getZ())*16.0);
            }
            case NORTH -> {
                x1 = 16-(int) ((location.getBlockX()-location.getX())*16.0);
                y1 = 16-(int) ((location.getBlockY()-location.getY())*16.0);
            }
            case EAST -> {
                x1 = 16-(int) ((location.getBlockZ()-location.getZ())*16.0);
                y1 = 16-(int) ((location.getBlockY()-location.getY())*16.0);
            }
            case SOUTH -> {
                x1 = (int) ((location.getBlockX()-location.getX())*16.0);
                y1 = 16-(int) ((location.getBlockY()-location.getY())*16.0);
            }
            case WEST -> {
                x1 = (int) ((location.getBlockZ()-location.getZ())*16.0);
                y1 = 16-(int) ((location.getBlockY()-location.getY())*16.0);
            }
        }

        if(x1 == -1 || y1 == -1) {
            return null;
        }

        return new IntIntImmutablePair(x1, y1);
    }

    public Vector transform(int x, int y, Vector location) {
        Vector vector = location.clone();

        vector.rotateAroundX(-x);
        vector.rotateAroundY(-y);

        return vector;
    }

    public BlockFace getFace(BlockFace original, int x, int y) {
        return transformed[original.ordinal()][x/90][y/90];
    }
}
