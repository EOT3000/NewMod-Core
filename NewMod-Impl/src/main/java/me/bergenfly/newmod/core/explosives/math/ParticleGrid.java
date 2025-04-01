package me.bergenfly.newmod.core.explosives.math;

import org.bukkit.World;

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


}
