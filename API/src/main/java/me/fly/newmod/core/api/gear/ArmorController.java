package me.fly.newmod.core.api.gear;

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
    /**
     * Calculate the damage reduction of this piece, for entity damage.
     *
     * @param damaged the entity being damaged.
     * @param rawDamage the raw damage before any reduction.
     * @param type the type of damage.
     * @param cause the entity that did the damage.
     * @return the calculated damage reduction.
     */
    int getDamageReductionEntity(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type, Entity cause);

    /**
     * Calculate the damage reduction of this piece, for block damage.
     *
     * @param damaged the entity being damaged.
     * @param rawDamage the raw damage before any reduction.
     * @param type the type of damage.
     * @param cause the block that did the damage.
     * @param causeState the state of the block that did the damage.
     * @return the calculated damage reduction.
     */
    int getDamageReductionBlock(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type, Block cause, BlockState causeState);

    /**
     * Calculate the damage reduction of this piece, for damage caused neither by entities, nor blocks, such as suffocation. //TODO: check if suffocation is actually of this category
     *
     * @param damaged the entity being damaged.
     * @param rawDamage the raw damage before any reduction.
     * @param type the type of damage.
     * @return the calculated damage reduction.
     */
    int getDamageReductionSelf(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type);
}
