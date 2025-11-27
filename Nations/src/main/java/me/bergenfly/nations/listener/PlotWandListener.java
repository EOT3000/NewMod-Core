package me.bergenfly.nations.listener;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.model.plot.Lot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class PlotWandListener implements Listener {

    public static Map<UUID, List<Lot.Rectangle>> rectangles = new HashMap<>();
    public static Map<UUID, IntIntPair> lefts = new HashMap<>(); //pos1
    public static Map<UUID, IntIntPair> rights = new HashMap<>(); //pos2
    public static Map<UUID, World> worlds = new HashMap<>();

    @EventHandler
    public void onWandClick(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null) {
            return;
        }

        if(event.getItem() == null) {
            return;
        }

        if(!event.getItem().getType().equals(Material.FLINT)/*TODO configurable*/) {
            return;
        }

        if(!event.getPlayer().isSneaking()) {
            return;
        }

        UUID pUUID = event.getPlayer().getUniqueId();

        if(worlds.containsKey(pUUID) && !event.getPlayer().getWorld().equals(worlds.get(pUUID))) {
            event.getPlayer().sendMessage(TranslatableString.translate("nations.selection.error.wrong_world"));
            return;
        }

        //rectangles.putIfAbsent(pUUID, new ArrayList<>());

        worlds.put(pUUID, event.getClickedBlock().getWorld());

        if(event.getAction().isLeftClick()) {
            lefts.put(pUUID, new IntIntImmutablePair(event.getClickedBlock().getX(), event.getClickedBlock().getZ()));
            event.getPlayer().sendMessage(TranslatableString.translate("nations.selection.pos1.success"));
        }

        if(event.getAction().isRightClick()) {
            rights.put(pUUID, new IntIntImmutablePair(event.getClickedBlock().getX(), event.getClickedBlock().getZ()));
            event.getPlayer().sendMessage(TranslatableString.translate("nations.selection.pos2.success"));
        }
    }
}
