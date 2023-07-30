package me.fly.newmod.flyfun.plants.block;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.block.ModBlockInstance;
import me.fly.newmod.core.api.block.data.ModBlockData;
import me.fly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import java.util.Random;

public class TeaPlant implements ModBlock {
    private final Random random = new Random();

    @Override
    public NamespacedKey getId() {
        return new NamespacedKey(FlyFunPlugin.get(), "tea_plant");
    }

    @Override
    public Material getMaterial() {
        return Material.OAK_SAPLING;
    }

    @Override
    public Class<? extends ModBlockData> getDataType() {
        return null;
    }

    @Override
    public boolean place(Block block, ModBlockInstance instance) {
        block.setType(Material.OAK_SAPLING);
        return true;
    }

    @Override
    public void tick(int tick, Block block, ModBlockInstance instance) {
        if(block.getType().equals(Material.AZALEA)) {
            //TODO: config this
            if(random.nextInt(1000) == 1) {
                block.setType(Material.FLOWERING_AZALEA);
            }
        }
    }

    @Override
    public Event.Result shouldDelete(Block block) {
        Material type = block.getType();

        return type == Material.OAK_SAPLING || type == Material.AZALEA || type == Material.FLOWERING_AZALEA ? Event.Result.ALLOW : Event.Result.DENY;
    }

    public void nextStage(Block block) {
        if(block.getType() == Material.OAK_SAPLING) {
            block.setType(Material.AZALEA);
        } else if(block.getType() == Material.AZALEA) {
            block.setType(Material.FLOWERING_AZALEA);
        }
    }
}
