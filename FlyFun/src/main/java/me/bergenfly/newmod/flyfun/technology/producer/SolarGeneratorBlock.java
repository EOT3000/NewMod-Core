package me.bergenfly.newmod.flyfun.technology.producer;

import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.technology.EnergyComponent;
import me.bergenfly.newmod.flyfun.technology.util.EnergyUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class SolarGeneratorBlock implements ModBlock, EnergyComponent {
    private final int baseMax, randomAdd, bonusForSide, capacity;

    private final NamespacedKey id;

    private static final Random random = new Random();

    private static final BlockManager bm = FlyFunPlugin.get().api.blockManager();

    public SolarGeneratorBlock(int baseMax, int randomAdd, int bonusForSide, int capacity, JavaPlugin plugin, String id) {
        this.id = new NamespacedKey(plugin, id);

        this.baseMax = baseMax;
        this.randomAdd = randomAdd;
        this.bonusForSide = bonusForSide;
        this.capacity = capacity;
    }

    @Override
    public NamespacedKey getId() {
        return id;
    }

    @Override
    public Material getMaterial() {
        return Material.DAYLIGHT_DETECTOR;
    }

    @Override
    public EnergyComponentType getType() {
        return EnergyComponentType.PRODUCER;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    public void tick(Block block) {
        //TODO: actual brightness
        int brightness = 12000;
        double power = (brightness / 12000.0) * baseMax + random.nextDouble() * randomAdd;

        int bonus = 0;

        if (isSP(block.getLocation(), BlockFace.NORTH)) {
            bonus += bonusForSide;
        }
        if (isSP(block.getLocation(), BlockFace.EAST)) {
            bonus += bonusForSide;
        }
        if (isSP(block.getLocation(), BlockFace.SOUTH)) {
            bonus += bonusForSide;
        }
        if (isSP(block.getLocation(), BlockFace.WEST)) {
            bonus += bonusForSide;
        }

        power += (bonus / 4.0) *
                (random.nextDouble() +
                        random.nextDouble() +
                        random.nextDouble() +
                        random.nextDouble());

        EnergyUtil.addCharge(block.getLocation(), (int) Math.round(power), capacity);

        if(bm.getType(block.getLocation().clone().subtract(0, 1, 0)) instanceof Object) {
        
        }
    }

    private boolean isSP(Location location, BlockFace face) {
        Location l = location.clone();

        return bm.getType(l.add(face.getModX(), face.getModY(), face.getModZ())) instanceof SolarGeneratorBlock;
    }


}

