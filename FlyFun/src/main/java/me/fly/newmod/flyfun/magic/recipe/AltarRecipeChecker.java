package me.fly.newmod.flyfun.magic.recipe;

import me.fly.newmod.flyfun.FlyFunPlugin;
import me.fly.newmod.flyfun.magic.block.altar.Pedestal;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class AltarRecipeChecker {
    public static AltarRecipe checkRecipe(Location location, ItemStack center) {
        for(Direction direction : Direction.values()) {
            ItemStack[] items = getItems(location, direction);

            items[0] = center;

            Set<AltarRecipe> set = FlyFunPlugin.get().getAltarRecipeManager().getRecipes(items[0], center);

            a: for(AltarRecipe recipe : set) {
                for(int i = 0; i < 9; i++) {
                    if(!recipe.getRecipe()[i].isSimilar(items[i])) {
                        continue a;
                    }
                }

                return recipe;
            }
        }

        return null;
    }

    private static ItemStack[] getItems(Location center, Direction start) {
        ItemStack[] stack = new ItemStack[9];

        for(int i = 0; i < 8; i++) {
            Direction check = Direction.values()[(i+start.ordinal())%8];

            stack[i+1] = Pedestal.getItem(check.addTo(center));
        }

        return stack;
    }

    public static void clear(Location location) {
        for(Direction direction : Direction.values()) {
            Pedestal.removeItemDisplay(direction.addTo(location));
            Pedestal.removeNameDisplay(direction.addTo(location));
        }
    }

    private enum Direction {
        NORTH(0, -3),
        NORTH_EAST(2, -2),
        EAST(3, 0),
        SOUTH_EAST(2, 2),
        SOUTH(0, 3),
        SOUTH_WEST(-2, 2),
        WEST(-3, 0),
        NORTH_WEST(-2, -2);

        private final int x;
        private final int z;

        Direction(int x, int z) {
            this.x = x;
            this.z = z;
        }

        private Location addTo(Location location) {
            return location.clone().add(x, 0, z);
        }
    }
}
