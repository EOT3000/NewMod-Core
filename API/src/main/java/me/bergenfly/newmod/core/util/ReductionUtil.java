package me.bergenfly.newmod.core.util;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//TODO: javadocs on util classes
//Following calculations all from Minecraft wiki
public class ReductionUtil {
    private static final List<DamageCause> resistanceImmune = Arrays.asList(
            DamageCause.VOID, DamageCause.CUSTOM, DamageCause.SUICIDE, DamageCause.STARVATION, DamageCause.SONIC_BOOM);

    private static final List<DamageCause> fire = Arrays.asList(
            DamageCause.FIRE_TICK, DamageCause.FIRE, DamageCause.LAVA, DamageCause.HOT_FLOOR);

    private static final List<DamageCause> blast = Arrays.asList(
            DamageCause.BLOCK_EXPLOSION, DamageCause.ENTITY_EXPLOSION);

    private static final List<DamageCause> proj = Arrays.asList(
            DamageCause.PROJECTILE);

    public static double armorModifier(double damage, int defense, int toughness) {
        double general = reductionArmor(damage, defense, toughness);

        return Math.min(general, damage);
    }

    public static double magicModifier(double damage, double armor, Map<Enchantment, Integer> enchantments, DamageCause type) {
        damage = damage-armor;

        double protection = reductionProtection(damage, enchantments.getOrDefault(Enchantment.PROTECTION_ENVIRONMENTAL, 0));
        double protectionFire = reductionBodySpecialProtection(damage, enchantments.getOrDefault(Enchantment.PROTECTION_FIRE, 0));
        double protectionProj = reductionBodySpecialProtection(damage, enchantments.getOrDefault(Enchantment.PROTECTION_PROJECTILE, 0));
        double protectionBlas = reductionBodySpecialProtection(damage, enchantments.getOrDefault(Enchantment.PROTECTION_EXPLOSIONS, 0));
        double protectionFall = reductionFeatherFalling(damage, enchantments.getOrDefault(Enchantment.PROTECTION_FALL, 0));

        if(resistanceImmune.contains(type)) {
            protection = 0;
        }

        if(!fire.contains(type)) {
            protectionFire = 0;
        }

        if(!blast.contains(type)) {
            protectionBlas = 0;
        }

        if(!proj.contains(type)) {
            protectionProj = 0;
        }

        if(!type.equals(DamageCause.FALL)) {
            protectionFall = 0;
        }

        return Math.min(protection+protectionFire+protectionProj+protectionBlas+protectionFall, damage);
    }

    public static double resistanceModifier(double damage, double armor, double magic, LivingEntity entity, DamageCause type) {
        damage = damage-armor-magic;

        double resistance = reductionResistance(damage, entity.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)
                ? entity.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier()
                : 0);

        if(resistanceImmune.contains(type)) {
            resistance = 0;
        }

        return Math.min(resistance, damage);
    }


    public static double reductionArmor(double damage, int defense, int toughness) {
        double secondCalc = defense - (4*damage)/(toughness+8);

        return damage - damage*(1-(Math.max(defense/5.0,secondCalc)/25));
    }

    public static double reductionResistance(double damage, int level) {
        return damage*(0.2*level);
    }

    public static double reductionProtection(double damage, int level) {
        return damage*(0.04*level);
    }

    //Blast Protection, Fire Protection, Projectile Protection
    public static double reductionBodySpecialProtection(double damage, int level) {
        return damage*(0.08*level);
    }

    public static double reductionFeatherFalling(double damage, int level) {
        return damage*(0.12*level);
    }
}
