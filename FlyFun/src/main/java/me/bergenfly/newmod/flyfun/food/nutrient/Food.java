package me.bergenfly.newmod.flyfun.food.nutrient;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.bergenfly.newmod.core.api.item.Item;

import java.util.HashMap;
import java.util.Map;

public class Food {
    private static final Map<Item, Food> foods = new HashMap<>();

    private final Object2IntMap<Nutrient> nutrients = new Object2IntOpenHashMap<>();

    private final int nutrition;
    private final float saturation;
    private final int size;
    private final int toughness;
    private final Item item;

    public Food(int nutrition, float saturation, int size, int toughness, Item item) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.size = size;
        this.toughness = toughness;

        foods.put(item, this);

        this.item = item;
    }

    public int getNutrition() {
        return nutrition;
    }

    public float getSaturation() {
        return saturation;
    }

    public int getSize() {
        return size;
    }

    public int getToughness() {
        return toughness;
    }

    public float consumeTimeSeconds() {
        return (float) (Math.round((((toughness+size)/10.0+toughness*size/10.0)+.3)*5)/5.0);
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

    public static Food getFood(Item item) {
        return foods.get(item);
    }
}
