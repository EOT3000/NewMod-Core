package me.fly.newmod.flyfun.magic.listener;

import me.fly.newmod.core.api.NewModAPI;
import me.fly.newmod.core.api.block.BlockManager;
import me.fly.newmod.core.api.block.ModBlock;
import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.magic.MagicTypes;
import me.fly.newmod.flyfun.magic.block.altar.Pedestal;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AltarListener implements Listener {
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockManager block = api.blockManager();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if(event.hasBlock() && event.getBlockFace().equals(BlockFace.UP)) {
            if(event.hasItem()) {
                ModBlock modBlock = block.getType(event.getClickedBlock());

                if (MagicTypes.ANCIENT_PEDESTAL.getBlock().equals(modBlock) && Pedestal.getItem(event.getClickedBlock().getLocation()) == null) {
                    Pedestal.setItemDisplay(event.getItem().asOne(), event.getClickedBlock().getLocation());
                    Pedestal.setNameDisplay(event.getItem().displayName(), event.getClickedBlock().getLocation());

                    ItemStack stack = event.getPlayer().getInventory().getItem(event.getHand());

                    if (stack.getAmount() == 1) {
                        event.getPlayer().getInventory().setItem(event.getHand(), null);
                    } else {
                        stack.setAmount(stack.getAmount() - 1);
                    }
                }
            } else if(!event.hasItem()) {
                ModBlock modBlock = block.getType(event.getClickedBlock());

                if (MagicTypes.ANCIENT_PEDESTAL.getBlock().equals(modBlock) && Pedestal.getItem(event.getClickedBlock().getLocation()) != null) {
                    ItemStack stack = Pedestal.getItem(event.getClickedBlock().getLocation());

                    if(event.getPlayer().getInventory().getItem(event.getHand()).getType().equals(Material.AIR)) {
                        event.getPlayer().getInventory().setItem(event.getHand(), stack);
                    } else {
                        event.getPlayer().getInventory().addItem(stack);
                    }

                    Pedestal.removeItemDisplay(event.getClickedBlock().getLocation());
                    Pedestal.removeNameDisplay(event.getClickedBlock().getLocation());
                }
            }
        }

        ModBlock modBlock = block.getType(event.getClickedBlock());

        if (MagicTypes.ANCIENT_PEDESTAL.getBlock().equals(modBlock)) {
            event.setCancelled(true);
        }
    }
}
