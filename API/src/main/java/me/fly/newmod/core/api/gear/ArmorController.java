package me.fly.newmod.core.api.gear;

import me.fly.newmod.core.util.IntTriple;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

/**
 * Manages the damage reduction for armor items.
 */
public interface ArmorController {
//TODO: split reduction among the three reduction categories (see original NM)
    IntTriple getReducedDamagesEntity(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type, Entity cause);

    IntTriple getReducedDamagesBlock(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type, Block cause, BlockState causeState);


    IntTriple getReducedDamagesSelf(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type);
}
