package me.fly.newmod.core.crafting;

import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.item.ModItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipeMatcher {
    public static boolean matches(ShapelessRecipe recipe, ItemStack[] stacks) {
        //TODO: make it smarter
        //ShapelessRecipe ro = recipe.getChoiceList();
        //System.out.println();
        List<ItemStack> so = fromArray(stacks);

        /*System.out.println("start");
        System.out.println("list: " + so);
        System.out.println("list size: " + so.size());
        System.out.println("choices: " + recipe.getChoiceList());
        System.out.println("choices size: " + recipe.getChoiceList().size());*/

        if(recipe.getChoiceList().size() != so.size()) {
            return false;
        }

        a: for(RecipeChoice choice : recipe.getChoiceList()) {
            //System.out.println("choice: " + choice);

            for(ItemStack stack : new ArrayList<>(so)) {
                //System.out.println("option: " + stack);
                if(matches(choice, stack, recipe)) {
                    //System.out.println("matches");
                    so.remove(stack);
                    //System.out.println("");

                    continue a;
                }
            }

            //System.out.println("left: " + so);

            return false;
        }

        //System.out.println("final left: " + so);

        return true;
    }

    private static boolean matches(RecipeChoice choice, ItemStack stack, ShapelessRecipe recipe) {
        ModItem type = NewModPlugin.get().itemManager().getType(stack);

        if(type == null) {
            return choice.test(stack);
        } else if(choice instanceof RecipeChoice.ExactChoice) {
            return choice.test(stack);
        }

        return false;
    }

    private static List<ItemStack> fromArray(ItemStack[] stacks) {
        List<ItemStack> ret = new ArrayList<>();

        for(ItemStack stack : stacks) {
            if(stack != null) {
                ret.add(stack);
            }
        }

        return ret;
    }
}
