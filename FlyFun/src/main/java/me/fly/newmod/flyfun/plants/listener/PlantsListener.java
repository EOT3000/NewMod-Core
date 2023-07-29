package me.fly.newmod.flyfun.plants.listener;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.block.BlockManager;
import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.core.api.blockstorage.BlockStorage;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.plants.PlantsTypes;
import me.fly.newmod.flyfun.plants.block.Seedling;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PlantsListener implements Listener {
    private final Random random = new Random();
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockManager block = api.blockManager();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack hand = event.getPlayer().getInventory().getItemInMainHand();

        if(hand.getType().equals(Material.SHEARS) || hand.hasEnchant(Enchantment.SILK_TOUCH)) {
            return;
        }

        int totalLuck = hand.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS)*2;

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
            case ACACIA_LEAVES -> dropItem(event.getBlock().getLocation(), PlantsTypes.ACACIA_SEEDS.create());
            case CHERRY_LEAVES -> dropItem(event.getBlock().getLocation(), PlantsTypes.RED_CHERRIES.create());
            case OAK_LEAVES,DARK_OAK_LEAVES -> dropItem(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
        }
    }

    private void dropItem(Location block, ItemStack item) {
        block.getWorld().dropItem(block, item);
    }

    @EventHandler
    public void onGrow(BlockGrowEvent event) {
        ModBlock b = block.getType(event.getBlock());

        if(b instanceof Seedling s) {
            s.grow(event.getBlock());
            event.setCancelled(true);
        }
    }
}
