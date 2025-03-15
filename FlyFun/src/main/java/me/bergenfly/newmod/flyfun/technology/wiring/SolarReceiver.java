package me.bergenfly.newmod.flyfun.technology.wiring;

import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.technology.EnergyComponent;
import me.bergenfly.newmod.flyfun.technology.util.EnergyUtil;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class SolarReceiver implements EnergyComponent {
    private static final BlockManager bm = FlyFunPlugin.get().api.blockManager();

    @Override
    public EnergyComponentType getType() {
        return EnergyComponentType.RECEIVER;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public Block getDestination(Block from, Block to, int stack) {
        for(int i = 0; i < 4; i++) {
            BlockFace face = BlockFace.values()[i];

            Block d = EnergyUtil.getDestination(to, face.getModX(), face.getModY(), face.getModZ(), stack);

            if(d != null) {
                return d;
            }
        }

        return null;
    }

    @Override
    public int push(Block from, Block to, int energy) {
        Block d = getDestination(from, to, 0);

        if(d == null) {
            return 0;
        }

        return ((EnergyComponent) bm.getType(d)).push(from, d, energy);
    }
}
