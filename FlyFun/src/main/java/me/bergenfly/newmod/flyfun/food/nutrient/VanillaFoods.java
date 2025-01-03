package me.bergenfly.newmod.flyfun.food.nutrient;

import org.bukkit.Material;

import static org.bukkit.Material.*;
import static me.bergenfly.newmod.core.api.item.VanillaItem.fromMaterial;

public class VanillaFoods {
    public static void init() {

    }

    //RAW MEAT
    public static final Food RAW_BEEF =         new Food(3, 1.8f, 5, 3, fromMaterial(BEEF));
    public static final Food RAW_PORK =         new Food(3, 1.8f, 5, 3, fromMaterial(PORKCHOP));
    public static final Food RAW_RABBIT =       new Food(3, 1.8f, 5, 3, fromMaterial(RABBIT));
    public static final Food RAW_MUTTON =       new Food(2, 1.2f, 5, 3, fromMaterial(MUTTON));
    public static final Food RAW_CHICKEN =      new Food(2, 1.2f, 5, 3, fromMaterial(CHICKEN));

    public static final Food RAW_COD =          new Food(1, 0.4f, 2, 3, fromMaterial(COD)); //Salmon is replaced with modded item
    public static final Food RAW_TROPICAL_FISH =new Food(1, 0.2f, 2, 3, fromMaterial(TROPICAL_FISH)); //Salmon is replaced with modded item

    //COOKED MEAT
    public static final Food COOKED_BEEF =      new Food(7, 12.8f, 4, 2, fromMaterial(Material.COOKED_BEEF));
    public static final Food COOKED_PORK =      new Food(7, 12.8f, 4, 2, fromMaterial(COOKED_PORKCHOP));
    public static final Food COOKED_RABBIT =    new Food(6, 6.0f, 4, 2, fromMaterial(Material.COOKED_RABBIT));
    public static final Food COOKED_MUTTON =    new Food(6, 9.6f, 4, 2, fromMaterial(Material.COOKED_MUTTON));
    public static final Food COOKED_CHICKEN =   new Food(6, 7.2f, 4, 2, fromMaterial(Material.COOKED_CHICKEN));

    public static final Food COOKED_COD =       new Food(4, 4.2f, 2, 2, fromMaterial(Material.COOKED_COD)); //Salmon is replaced with modded item

    //VEGETABLES
    public static final Food POTATO =           new Food(1, 0.6f, 2, 3, fromMaterial(Material.POTATO));
    public static final Food CARROT =           new Food(2, 2.4f, 1, 2, fromMaterial(Material.CARROT));
    public static final Food BEETROOT =         new Food(1, 0.8f, 2, 3, fromMaterial(Material.BEETROOT));

    //FRUITS
    public static final Food APPLE =            new Food(4, 2.4f, 2, 2, fromMaterial(Material.APPLE));
    public static final Food MELON =            new Food(2, 1.8f, 2, 1, fromMaterial(Material.MELON));
    public static final Food SWEET_BERRIES =    new Food(2, 0.4f, 1, 1, fromMaterial(Material.SWEET_BERRIES));
    public static final Food GLOW_BERRIES =     new Food(2, 0.4f, 1, 1, fromMaterial(Material.GLOW_BERRIES));
    public static final Food CHORUS_FRUIT =     new Food(4, 2.4f, 2, 1, fromMaterial(Material.CHORUS_FRUIT));

    //GOLDEN
    public static final Food GOLDEN_CARROT =    new Food(6, 14.4f, 2, 3, fromMaterial(Material.GOLDEN_CARROT));
    public static final Food GOLDEN_APPLE =     new Food(4, 9.6f, 2, 3, fromMaterial(Material.GOLDEN_APPLE));
    public static final Food ENCHANTED_GOLDEN_APPLE = new Food(4, 9.6f, 2, 3, fromMaterial(Material.ENCHANTED_GOLDEN_APPLE));

    //COOKED PLANTS
    public static final Food POPPED_CHORUS_FRUIT = new Food(1, 0.4f, 2, 4, fromMaterial(Material.POPPED_CHORUS_FRUIT));
    public static final Food BAKED_POTATO =     new Food(4, 5.0f, 2, 2, fromMaterial(Material.BAKED_POTATO));
    public static final Food DRIED_KELP =       new Food(1, 1.2f, 1, 0, fromMaterial(Material.DRIED_KELP));


    //Cooked Wheat/Flour -> 1.67 hunger, 1.5 saturation, size 1
    //Egg -> 1 hunger, 2 saturation, absorbs into 1 flour, otherwise size 1
    //Sugar -> 2 hunger, 0 saturation, size 1
    //Milk -> 1 hunger, 0.5 saturation
    //Cocoa beans -> 0.5 hunger, 0.2 saturation, size 1
    //Pumpkin insides -> 1.5 hunger, 0.2 saturation, size 1
    //Pie crust (1 flour)

    //Cookie -> 3 flour, 2 sugar, 1 cocoa beans -> 5 cookies
    //2 hunger, .9 saturation each
    //size 1
    public static final Food COOKIE =           new Food(2, 0.8f, 2, 1, fromMaterial(Material.COOKIE));

    public static final Food BREAD =            new Food(5, 4.6f, 3, 1, fromMaterial(Material.BREAD));

    //Cake -> 3 flour, 2 sugar, 1 egg, 2 milk, 1 berries
    //14 hunger, 8 saturation
    public static final Food CAKE =             new Food(14, 8.0f, -1, -1, fromMaterial(Material.CAKE));

    //Pumpkin pie -> 1 pie crust, 1 sugar, 1 pumpkin insides, 1 milk, 1 egg
    //7 hunger, 4.4 saturation
    //size 4
    public static final Food PUMPKIN_PIE =      new Food(7, 4.4f, 4, 1, fromMaterial(Material.PUMPKIN_PIE));

    //STEWS

    //Cooked Mushroom -> 2 hunger, 2.4 saturation, size 1

    //Beetroot stew -> 2 beetroot, 1 potato, 1 carrot
    //12 hunger, 10 saturation
    //size 6
    public static final Food BEETROOT_SOUP =    new Food(12, 10.0f, 6, 1, fromMaterial(Material.BEETROOT_SOUP));

    //Rabbit stew -> 1 half rabbit, 1 carrot, 1 potato, 1 mushroom
    //14 hunger, 14 saturation
    //size 6
    public static final Food RABBIT_STEW =      new Food(14, 14.0f, 6, 2, fromMaterial(Material.RABBIT_STEW));

    //Mushroom stew -> 6 mushrooms
    //12 hunger, 15 saturation
    //size 6
    public static final Food MUSHROOM_STEW =    new Food(12, 14.4f, 6, 1, fromMaterial(Material.MUSHROOM_STEW));

    //Mushroom stew -> 2 mushrooms, 3 flower
    //7 hunger, 6 saturation
    //size 5
    public static final Food SUSPICIOUS_STEW =  new Food(7, 6.0f, 5, 0, fromMaterial(Material.SUSPICIOUS_STEW));

    //OTHER
    public static final Food HONEY =            new Food(6, 1.2f, 3, 1, fromMaterial(HONEY_BOTTLE));

    public static final Food SPIDER_EYE =       new Food(2, 3.2f, 1, 2, fromMaterial(Material.SPIDER_EYE));
    public static final Food ROTTEN_FLESH =     new Food(2, 0.2f, 4, 2, fromMaterial(Material.ROTTEN_FLESH));
    public static final Food POISONOUS_POTATO = new Food(1, 0.4f, 2, 2, fromMaterial(Material.POISONOUS_POTATO));
    public static final Food PUFFERFISH =       new Food(1, 0.2f, 2, 4, fromMaterial(Material.PUFFERFISH));
}
