package me.bergenfly.newmod.flyfun.food.nutrient;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.bergenfly.newmod.core.api.item.Item;

public class Food {
    private final Object2IntMap<Nutrient> nutrients = new Object2IntOpenHashMap<>();

    private final int hungerPoints;
    private final float saturation;
    private final int size;
    private final int toughness;
    private final Item item;

    public Food(int hungerPoints, float saturation, int size, int toughness, Item item) {
        this.hungerPoints = hungerPoints;
        this.saturation = saturation;
        this.size = size;
        this.toughness = toughness;

        this.item = item;
    }

    public int getHungerPoints() {
        return hungerPoints;
    }

    public float getSaturation() {
        return saturation;
    }

    public int getSize() {
        return size;
    }

    public Item getItem() {
        return item;
    }

    public int getNutrientLevel(Nutrient nutrient) {
        return nutrients.getOrDefault(nutrient, 0);
    }

    public Food setNutrient(Nutrient nutrient, int amount) {
        nutrients.put(nutrient, amount);

        return this;
    }
}
