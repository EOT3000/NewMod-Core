package me.fly.newmod.flyfun.technology.link;

import me.fly.newmod.api.block.BlockEventsListener;
import me.fly.newmod.api.block.ModBlockType;
import me.fly.newmod.api.events.block.ModBlockTickEvent;
import me.fly.newmod.technology.TechnologyPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import java.util.Random;

public class CableBlockType extends ModBlockType {
    public final boolean damage;

    private final Random random = new Random();

    public CableBlockType(Material mat, boolean damage, String id) {
        this(mat, damage, TechnologyPlugin.get(), id);
    }

    public CableBlockType(Material mat, boolean damage, JavaPlugin plugin, String id) {
        super(mat, new NamespacedKey(plugin, id));

        this.damage = damage;

        if(damage) {
            setListener(new BlockEventsListener() {
                @Override
                public void onBlockTick(ModBlockTickEvent event) {
                    tick(event);
                }
            });
        }
    }

    private void tick(ModBlockTickEvent event) {
        double e = 1.0/16;

        BoundingBox b = event.getBlock().getBoundingBox().expand(e, e, e, e, e, e);
        World w = event.getBlock().getWorld();

        for(Entity entity : w.getNearbyEntities(b)) {
            if(entity instanceof LivingEntity l) {
                if(event.getTick() % 7 == random.nextInt(7)) {
                    l.damage(1);
                }
            }
        }
    }
}
