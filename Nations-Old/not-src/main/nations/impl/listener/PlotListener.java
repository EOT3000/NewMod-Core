package me.bergenfly.nations.listener;

import it.unimi.dsi.fastutil.Pair;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.api.manager.NationsLandManager;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PermissiblePlotSection;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.permission.DefaultPlotPermission;
import me.bergenfly.nations.api.permission.PlotPermission;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlotListener implements Listener {
    private final NationsPlugin api = NationsPlugin.getInstance();
    private final NationsLandManager landManager = api.landManager();

    private final List<Pair<UUID, PlotPermission>> locks = new ArrayList<>();


    private void checkBuild(Block block, Cancellable cancellable, Player player) {
        PlotSection section = landManager.getPlotSectionAtLocation(block.getLocation());

        if (section instanceof PermissiblePlotSection permissiblePlotSection) {
            User user = api.usersRegistry().get(player.getUniqueId());

            if (!permissiblePlotSection.hasPermission(DefaultPlotPermission.BUILD, user)) {
                sendMessage(DefaultPlotPermission.BUILD, player);
                cancellable.setCancelled(true);
            }
        }
    }

    private void sendMessage(PlotPermission permission, Player player) {
        Pair<UUID, PlotPermission> p = Pair.of(player.getUniqueId(), permission);

        if(locks.contains(Pair.of(player.getUniqueId(), permission))) return;

        player.sendMessage(TranslatableString.translate("nations.general.no_permission"));

        locks.add(p);

        Bukkit.getScheduler().runTaskLater(api, () -> locks.remove(p), 20*5);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        checkBuild(event.getBlock(), event, event.getPlayer());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        checkBuild(event.getBlock(), event, event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block cb = event.getClickedBlock();

        //Block clicks
        if(cb != null) {
            Material type = cb.getType();

            PlotSection section = landManager.getPlotSectionAtLocation(cb.getLocation());

            if (section instanceof PermissiblePlotSection permissiblePlotSection) {
                User user = api.usersRegistry().get(event.getPlayer().getUniqueId());

                if (Tag.FENCE_GATES.isTagged(cb.getType())
                        || Tag.DOORS.isTagged(cb.getType())
                        || Tag.TRAPDOORS.isTagged(cb.getType())) {
                    if (!permissiblePlotSection.hasPermission(DefaultPlotPermission.DOOR, user)) {
                        sendMessage(DefaultPlotPermission.DOOR, event.getPlayer());
                        event.setCancelled(true);
                      }
                }

                if (Tag.BUTTONS.isTagged(cb.getType())
                        || cb.getType().equals(Material.LEVER)) {
                    if (!permissiblePlotSection.hasPermission(DefaultPlotPermission.LEVER, user)) {
                        sendMessage(DefaultPlotPermission.LEVER, event.getPlayer());
                        event.setCancelled(true);
                        return;
                    }
                }

                if(type == Material.CHEST
                        || type == Material.TRAPPED_CHEST
                        || type == Material.BARREL
                        || type == Material.HOPPER
                        || type == Material.DISPENSER
                        || type == Material.DROPPER
                        || type == Material.BREWING_STAND
                        || type == Material.SMOKER
                        || type == Material.FURNACE
                        || type == Material.BLAST_FURNACE
                        || type == Material.CRAFTER
                        || Tag.SHULKER_BOXES.isTagged(type)) {
                    if (!permissiblePlotSection.hasPermission(DefaultPlotPermission.CONTAINER, user)) {
                        sendMessage(DefaultPlotPermission.CONTAINER, event.getPlayer());
                        event.setCancelled(true);
                        return;
                    }
                }

                if(Tag.ALL_SIGNS.isTagged(type)) {
                    if (!permissiblePlotSection.hasPermission(DefaultPlotPermission.SIGN, user)) {
                        sendMessage(DefaultPlotPermission.SIGN, event.getPlayer());
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
