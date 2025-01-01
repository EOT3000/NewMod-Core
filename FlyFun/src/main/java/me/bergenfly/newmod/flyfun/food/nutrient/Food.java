package me.bergenfly.newmod.flyfun.food.nutrient;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class Food {
    private final Object2IntMap<Nutrient> nutrients = new Object2IntOpenHashMap<>();

    private final int hungerPoints;
    private final int saturation;
    private final int size;

    public Food(int hungerPoints, int saturation, int size) {
        this.hungerPoints = hungerPoints;
        this.saturation = saturation;
        this.size = size;
    }

    public int getHungerPoints() {
        return hungerPoints;
    }

    public int getSaturation() {
        return saturation;
    }

    public Food setNutrient(Nutrient nutrient, int amount) {
        nutrients.put(nutrient, amount);

        return this;
    }
}
