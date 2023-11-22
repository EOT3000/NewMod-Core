package me.fly.newmod.core.armor;

import me.fly.newmod.core.api.gear.ArmorController;
import me.fly.newmod.core.api.gear.ArmorMaterial;
import me.fly.newmod.core.api.gear.GearManager;
import me.fly.newmod.core.util.DamageChecker;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class PlainArmorController implements ArmorController {
    private final GearManager.ArmorSection section;

    //TODO: finish this

    public PlainArmorController(GearManager.ArmorSection section) {
        this.section = section;
    }

    @Override
    public int getDamageReductionEntity(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type, Entity cause) {
        return 0;
    }

    @Override
    public int getDamageReductionBlock(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type, Block cause, BlockState causeState) {
        if(type.equals(EntityDamageEvent.DamageCause.LAVA)) {
            List<GearManager.ArmorSection> lava = DamageChecker.affectsLava(damaged);

            if(lava.contains(section)) {
                return rawDamage;
            } else {
                return 0;
            }
        }

        return 0;
    }

    @Override
    public int getDamageReductionSelf(Entity damaged, int rawDamage, EntityDamageEvent.DamageCause type) {
        return 0;
    }
}
