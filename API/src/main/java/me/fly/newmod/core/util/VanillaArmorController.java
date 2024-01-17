package me.fly.newmod.core.util;

import me.fly.newmod.core.api.gear.ArmorController;
import me.fly.newmod.core.api.gear.ArmorMaterial;
import me.fly.newmod.core.api.gear.GearManager;
import me.fly.newmod.core.util.DamageChecker;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class VanillaArmorController implements ArmorController {
    private final GearManager.ArmorSection section;
    private final int defense, toughness;

    //TODO: finish this

    public VanillaArmorController(GearManager.ArmorSection section, int defense, int toughness) {
        this.section = section;

        this.defense = defense;
        this.toughness = toughness;
    }

    @Override
    public IntTriple getReducedDamagesEntity(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type, Entity cause) {
        return null;
    }

    @Override
    public IntTriple getReducedDamagesBlock(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type, Block cause, BlockState causeState) {
        return switch (type) {
            //Vanilla armor does not reduce the following damage
            case VOID, CUSTOM, SUICIDE, STARVATION, SONIC_BOOM -> null;
            default -> null;
        };

    }

    @Override
    public IntTriple getReducedDamagesSelf(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type) {
        return null;
    }
}
