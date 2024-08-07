package me.bergenfly.newmod.flyfun.plants.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.block.ModBlock;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.plants.PlantsTypes;
import me.bergenfly.newmod.flyfun.plants.block.Seedling;
import me.bergenfly.newmod.flyfun.plants.block.TeaPlant;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PlantsListener implements Listener {
    private final Random random = new Random();
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockManager block = api.blockManager();
    private static final BlockStorage blockStore = api.blockStorage();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack hand = event.getPlayer().getInventory().getItemInMainHand();

        if(hand.getType().equals(Material.SHEARS) || hand.hasEnchant(Enchantment.SILK_TOUCH)) {
            return;
        }

        int totalLuck = 0;

        if(hand.hasItemMeta()) {
            totalLuck = hand.getEnchantLevel(Enchantment.LOOTING)*2;
        }

        PotionEffect luck = event.getPlayer().getPotionEffect(PotionEffectType.LUCK);
        PotionEffect unluck = event.getPlayer().getPotionEffect(PotionEffectType.UNLUCK);

        if(luck != null) {
            totalLuck+=luck.getAmplifier()*2;
        }

        if(unluck != null) {
            totalLuck-=unluck.getAmplifier()*2;
        }

        float chance = 0;

        if(totalLuck != 20) {
            chance = 1/(20f-totalLuck);
        }

        if(random.nextDouble() > chance) {
            return;
        }

        switch (event.getBlock().getType()) {
            case ACACIA_LEAVES -> dropItemNaturally(event.getBlock().getLocation(), PlantsTypes.WATTLESEED.create());
            case CHERRY_LEAVES -> dropItemNaturally(event.getBlock().getLocation(), PlantsTypes.RED_CHERRIES.create());
            case OAK_LEAVES,DARK_OAK_LEAVES -> dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
        }
    }

    private void dropItemNaturally(Location block, ItemStack item) {
        block.getWorld().dropItemNaturally(block, item);
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        ModBlock b = block.getType(event.getLocation());

        //TODO: expand this to any bush
        if(b instanceof TeaPlant t) {
            t.nextStage(event.getLocation().getBlock());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGrow(BlockGrowEvent event) {
        ModBlock b = block.getType(event.getBlock());

        if(b instanceof Seedling s) {
            Ageable ageable = (Ageable) event.getBlock().getBlockData();

            if(ageable.getAge() >= 6) {
                s.grow(event.getBlock());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFromTo(BlockFromToEvent event) {
        Block bl = event.getToBlock();
        ModBlock b = block.getType(bl);

        if(b instanceof Seedling s) {
            event.setCancelled(true);
            bl.setType(Material.AIR);
            //TODO: easier clear data method
            blockStore.getBlock(bl.getLocation()).removeAllData(BlockStorage.StorageType.BLOCK_DATA);

            dropItemNaturally(bl.getLocation(), s.drop);
        }
    }

    @EventHandler
    public void onDestroy(BlockDestroyEvent event) {
        ModBlock b = block.getType(event.getBlock());
        Block bl = event.getBlock();

        if(b instanceof Seedling s) {
            event.setWillDrop(false);
            bl.setType(Material.AIR);
            blockStore.getBlock(bl.getLocation()).removeAllData(BlockStorage.StorageType.BLOCK_DATA);
            dropItemNaturally(bl.getLocation(), s.drop);
        } else if(b instanceof TeaPlant) {
            event.setWillDrop(false);
            bl.setType(Material.AIR);
            blockStore.getBlock(bl.getLocation()).removeAllData(BlockStorage.StorageType.BLOCK_DATA);
            for(ItemStack stack : b.getDrops(bl, null)) {
                dropItemNaturally(bl.getLocation(), stack);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block cb = event.getClickedBlock();
        ModBlock b = block.getType(cb);

        //TODO: fortune and shears and whatever else

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if(b instanceof TeaPlant) {
            if(cb.getType() == Material.AZALEA) {
                dropItemNaturally(cb.getLocation(), PlantsTypes.UNRIPE_TEA_LEAF.create());

                if(random.nextBoolean()) {
                    dropItemNaturally(cb.getLocation(), PlantsTypes.UNRIPE_TEA_LEAF.create());
                }

                cb.setType(Material.OAK_SAPLING);
            } else if(cb.getType() == Material.FLOWERING_AZALEA) {
                dropItemNaturally(cb.getLocation(), PlantsTypes.RIPE_TEA_LEAF.create());
                dropItemNaturally(cb.getLocation(), PlantsTypes.TEA_SEEDS.create());

                if(random.nextBoolean()) {
                    dropItemNaturally(cb.getLocation(), PlantsTypes.RIPE_TEA_LEAF.create());
                }

                cb.setType(Material.OAK_SAPLING);
            }
        }
    }
}
