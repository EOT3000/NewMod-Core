package me.fly.newmod.flyfun.magic.block.altar;

import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.magic.MagicTypes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class AncientPedestal implements ModBlock {
    private final NamespacedKey id;
    private final Material material;

    public AncientPedestal() {
        this.id = new NamespacedKey(FlyFunPlugin.get(), "ancient_pedestal");
        this.material = Material.DISPENSER;
    }

    @Override
    public NamespacedKey getId() {
        return id;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public void place0(Block block) {
        Directional data = (Directional) block.getBlockData();

        data.setFacing(BlockFace.UP);

        block.setBlockData(data);
    }

    @Override
    public List<ItemStack> getDrops(Block block, Player breaker) {
        return Collections.singletonList(MagicTypes.ANCIENT_PEDESTAL.create());
    }
}
