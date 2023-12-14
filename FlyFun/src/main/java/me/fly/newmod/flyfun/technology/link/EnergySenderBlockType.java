package me.fly.newmod.flyfun.technology.link;

import me.fly.newmod.NewMod;
import me.fly.newmod.api.block.ModBlockType;
import me.fly.newmod.api.events.block.ModBlockTickEvent;
import me.fly.newmod.technology.EnergyComponent;
import me.fly.newmod.technology.TechnologyPlugin;
import me.fly.newmod.technology.data.EnergyHolderBlockData;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Orientable;

import java.util.ArrayList;
import java.util.List;

public class EnergySenderBlockType extends ModBlockType implements EnergyComponent {
    public EnergySenderBlockType() {
        super(Material.TARGET, new NamespacedKey(TechnologyPlugin.get(), "energy_sender"));
    }

    @Override
    public EnergyComponentType getType() {
        return EnergyComponentType.SENDER;
    }

    @Override
    public int getCapacity() {
        return 1000000;
    }

    private void tick(ModBlockTickEvent event) {
        EnergyHolderBlockData data = (EnergyHolderBlockData) event.getModBlock().getData();

        List<Location> list = new ArrayList<>();

        list.add(next(event.getBlock().getLocation(), Axis.X, 1, 10));
        list.add(next(event.getBlock().getLocation(), Axis.X, -1, 10));
        list.add(next(event.getBlock().getLocation(), Axis.Y, 1, 10));
        list.add(next(event.getBlock().getLocation(), Axis.Y, -1, 10));
        list.add(next(event.getBlock().getLocation(), Axis.Z, 1, 10));
        list.add(next(event.getBlock().getLocation(), Axis.Z, -1, 10));

        for (Location location : list) {
            if (location == null) {
                continue;
            }


        }
    }

    private Location next(Location location, Axis axis, int direction, int deep) {
        if (deep-- == 0) {
            return null;
        }

        Location n = switch (axis) {
            case X -> location.clone().add(direction, 0, 0);
            case Y -> location.clone().add(0, direction, 0);
            case Z -> location.clone().add(0, 0, direction);
        };

        BlockState state = n.getBlock().getState();

        if (state instanceof Orientable orientable) {
            if (!orientable.getAxis().equals(axis)) {
                return null;
            }
        }

        if (!(NewMod.get().getBlockManager().getType(n.getBlock()) instanceof CableBlockType)) {
            return null;
        }

        if (NewMod.get().getBlockManager().getType(n.getBlock()) instanceof EnergyReceiverBlockType) {
            return n;
        }

        return next(n, axis, direction, deep);
    }
}
