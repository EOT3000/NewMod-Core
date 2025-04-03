package me.bergenfly.newmod.core.explosives.math;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import org.bukkit.Location;

import java.util.function.IntConsumer;

public class ParticleGrid {
    static {
        BOXES_PER_BLOCK_LENGTH = 4;
        FACTOR_PARTICLES = 1000;
    }

    public static final int BOXES_PER_BLOCK_LENGTH;
    public static final int FACTOR_PARTICLES;
    public static final double TOTAL_FACTOR = ((double) BOXES_PER_BLOCK_LENGTH)*FACTOR_PARTICLES;

    //private final World world;

    private int minBlockX;
    private int minBlockY;
    private int minBlockZ;

    private ParticleBox[][][] grid;

    private int sizeX = 100;
    private int sizeY = 100;
    private int sizeZ = 100;

    public ParticleBox getAt(vec3 location) {
        int x = (int) Math.floor(location.a*BOXES_PER_BLOCK_LENGTH)-minBlockX*BOXES_PER_BLOCK_LENGTH;
        int y = (int) Math.floor(location.b*BOXES_PER_BLOCK_LENGTH)-minBlockY*BOXES_PER_BLOCK_LENGTH;
        int z = (int) Math.floor(location.c*BOXES_PER_BLOCK_LENGTH)-minBlockZ*BOXES_PER_BLOCK_LENGTH;

        if(resizeIfNeeded(x,y,z)) {
            return getAt(location);
        }

        if(grid[x][y][z] == null) {
            return grid[x][y][z] = new ParticleBox();
        }

        return grid[x][y][z];
    }

    private boolean resizeIfNeeded(int x, int y, int z) {
        if(x < 0) {
            grid = doResize(grid, (a) -> new ParticleBox[a][sizeY][sizeZ], (a) -> minBlockX-=a, x);
            sizeX = grid.length;

            return true;
        }

        if(x > sizeX) {
            grid = doResize(grid, (a) -> new ParticleBox[a][sizeY][sizeZ], (a) -> {}, x-sizeX);
            sizeX = grid.length;

            return true;
        }

        if(y < 0) {
            for(int xi = 0; xi < sizeX; xi++) {
                ParticleBox[][] firstLayer = grid[xi];

                firstLayer = doResize(firstLayer, (a) -> new ParticleBox[a][sizeZ], (a) -> minBlockY-=a, y);
                sizeY = firstLayer.length;
            }

            return true;
        }

        if(y > sizeY) {
            for(int xi = 0; xi < sizeX; xi++) {
                ParticleBox[][] firstLayer = grid[xi];

                firstLayer = doResize(firstLayer, (a) -> new ParticleBox[a][sizeZ], (a) -> {}, y-sizeY);
                sizeY = firstLayer.length;
            }

            return true;
        }

        if(z < 0) {
            for(int xi = 0; xi < sizeX; xi++) {
                ParticleBox[][] firstLayer = grid[xi];

                for(int yi = 0; yi < sizeY; yi++) {
                    ParticleBox[] secondLayer = firstLayer[yi];

                    secondLayer = doResize(secondLayer, ParticleBox[]::new, (a) -> minBlockZ -= a, z);
                    sizeZ = secondLayer.length;
                }
            }

            return true;
        }

        if(z > sizeZ) {
            for(int xi = 0; xi < sizeX; xi++) {
                ParticleBox[][] firstLayer = grid[xi];

                for(int yi = 0; yi < sizeY; yi++) {
                    ParticleBox[] secondLayer = firstLayer[yi];

                    secondLayer = doResize(secondLayer, ParticleBox[]::new, (a) -> {}, z-sizeZ);
                    sizeZ = secondLayer.length;
                }
            }

            return true;
        }

        return false;
    }

    private <T> T doResize(Object[] array, Int2ObjectFunction<T> function, IntConsumer replacer, int add) {
        if(add < 0) {
            int changeBlock = ((-add) / BOXES_PER_BLOCK_LENGTH + 1);
            int change = changeBlock * BOXES_PER_BLOCK_LENGTH;

            T newGrid = function.apply(array.length+change);

            System.arraycopy(array, 0, newGrid, change, array.length);

            //sizeY += change;
            //minBlockY -= changeBlock;
            replacer.accept(changeBlock);

            return newGrid;
        } else {
            int changeBlock = ((add) / BOXES_PER_BLOCK_LENGTH + 1);
            int change = changeBlock * BOXES_PER_BLOCK_LENGTH;

            T newGrid = function.apply(array.length+change);

            System.arraycopy(array, 0, newGrid, 0, array.length);

            //sizeY += change;
            //minBlockY -= changeBlock;
            replacer.accept(changeBlock);

            return newGrid;
        }
    }
}
