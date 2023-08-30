package me.fly.newmod.flyfun.plants.block;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.plants.PlantsTypes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
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
    public void place(Block block) {
        block.setType(Material.OAK_SAPLING);
    }

    @Override
    public void tick(int tick, Block block) {
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

    @Override
    public List<ItemStack> getDrops(Block block, Player breaker) {
        List<ItemStack> ret = new ArrayList<>();

        ret.add(PlantsTypes.TEA_SAPLING.create());

        //TODO: player stuff

        if(block.getType() == Material.AZALEA) {
            ret.add(PlantsTypes.UNRIPE_TEA_LEAF.create());

            if(random.nextBoolean()) {
                ret.add(PlantsTypes.UNRIPE_TEA_LEAF.create());
            }
        } else if(block.getType() == Material.FLOWERING_AZALEA) {
            ret.add(PlantsTypes.RIPE_TEA_LEAF.create());
            ret.add(PlantsTypes.TEA_SEEDS.create());

            if(random.nextBoolean()) {
                ret.add(PlantsTypes.RIPE_TEA_LEAF.create());
            }
        }

        return ret;
    }

    public void nextStage(Block block) {
        if(block.getType() == Material.OAK_SAPLING) {
            block.setType(Material.AZALEA);
        } else if(block.getType() == Material.AZALEA) {
            block.setType(Material.FLOWERING_AZALEA);
        }
    }
}
