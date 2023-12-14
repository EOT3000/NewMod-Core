package me.fly.newmod.flyfun.technology.link;

import me.fly.newmod.api.block.ModBlockType;
import me.fly.newmod.technology.EnergyComponent;
import me.fly.newmod.technology.TechnologyPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public class EnergyReceiverBlockType extends ModBlockType implements EnergyComponent {
    public EnergyReceiverBlockType() {
        super(Material.TARGET, new NamespacedKey(TechnologyPlugin.get(), "energy_receiver"));
    }

    @Override
    public EnergyComponentType getType() {
        return EnergyComponentType.RECEIVER;
    }

    @Override
    public int getCapacity() {
        return 1000000;
    }
}
