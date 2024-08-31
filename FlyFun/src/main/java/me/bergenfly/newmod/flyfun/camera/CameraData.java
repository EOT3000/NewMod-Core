package me.bergenfly.newmod.flyfun.camera;

import me.bergenfly.newmod.core.util.Pair;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.Map;

public class CameraData {
    private final Map<Pair<Material, Integer>, Integer> dictionary = new HashMap<>();

    private final int[][] data = new int[256][256];

    public void addPixel(int x, int y, Material material, BlockState state, BlockData data) {

    }
}
