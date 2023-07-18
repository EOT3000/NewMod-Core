package me.fly.newmod.core.listeners;

import me.fly.newmod.core.NewModPlugin;
import me.fly.newmod.core.api.item.ModItem;
import me.fly.newmod.core.crafting.ShapedRecipeMatcher;
import me.fly.newmod.core.crafting.ShapelessRecipeMatcher;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

public class VanillaReplacementListener implements Listener {
    private final int[][] tableItems = new int[][]
                    {{0,0},{0,1},{0,2},
                    {1,0},{1,1},{1,2},
                    {2,0},{2,1},{2,2}};

    private int count = 0;

    public VanillaReplacementListener() {
        System.out.println("created");
    }

    /*@EventHandler(ignoreCancelled = true)
    @SuppressWarnings({"ConstantConditions", "PatternVariableCanBeUsed"})
    public void onPreCraftE(PrepareItemCraftEvent event) {
        try {
            ItemManager manager = NewMod.get().getItemManager();

            if (event.getRecipe() instanceof ShapedRecipe) {
                if (!event.getInventory().getType().equals(InventoryType.WORKBENCH)) {
                    checkForAny(event);
                    return;
                }

                ShapedRecipe recipe = (ShapedRecipe) event.getRecipe();

                for (int i = 0; i < 9; i++) {
                    char letter = recipe.getShape()[tableItems[i][0]].charAt(tableItems[i][1]);
                    ItemStack stack = recipe.getIngredientMap().get(letter);

                    ModItemType rtype = manager.getType(event.getInventory().getMatrix()[i]);
                    ModItemType stype = manager.getType(stack);

                    if (rtype == null) {
                        if (stype != null && !stype.isCraftable()) {
                            event.getInventory().setResult(new ItemStack(Material.AIR));
                            return;
                        }
                    } else {
                        if (stype != null) {
                            ModItemStack rstack = new ModItemStack(event.getInventory().getMatrix()[i]);
                            ModItemStack sstack = new ModItemStack(stack);

                            if (!rstack.getMeta().isAcceptable(sstack.getMeta())) {
                                event.getInventory().setResult(new ItemStack(Material.AIR));
                                return;
                            }
                        } else {
                            event.getInventory().setResult(new ItemStack(Material.AIR));
                            return;
                        }
                    }

                }
            } else {
                checkForAny(event);
            }
        } catch (Exception e) {
            if(count++ % 50 == 0) {
                e.printStackTrace();
            }
        }
    }*/

    @EventHandler
    public void onPreCraft(PrepareItemCraftEvent event) {
        if(event.getRecipe() instanceof ShapedRecipe) {
            if(!ShapedRecipeMatcher.matches(event.getInventory())) {
                event.getInventory().setResult(null);
            }
        } else if(event.getRecipe() instanceof ShapelessRecipe recipe) {
            if(!ShapelessRecipeMatcher.matches(recipe, event.getInventory().getMatrix())) {
                event.getInventory().setResult(null);
            }
        }
    }

    @EventHandler
    public void onPreSmelt(FurnaceBurnEvent event) {
        /*if(event.getRecipe() instanceof ModFurnaceRecipe recipe) {
            if(!recipe.canBeUsed(new ModBlock(event.getBlock()).getType())) {
                Furnace furnace = (Furnace) event.getBlock().getState();

                furnace.setBurnTime((short) -1);

                furnace.update();

                return;
            }
        }*/

        FurnaceInventory inventory = ((Furnace) event.getBlock().getState()).getInventory();

        itc(event, inventory.getSmelting());
        itc(event, inventory.getFuel());

        /*if(type.getProperties() instanceof CraftingProperties properties) {
            //TODO: other smelting types
            if(!properties.isAllRecipes() && !properties.getRecipes().contains(CraftingProperties.SMELTING)) {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }*/
    }

    private void itc(Cancellable cancellable, ItemStack stack) {
        ModItem type = NewModPlugin.get().itemManager().getType(stack);

        if(type == null) {
            return;
        }

        cancellable.setCancelled(true);
    }

    private void itc2(SmithingInventory inventory, ItemStack stack) {
        ModItem type = NewModPlugin.get().itemManager().getType(stack);

        if(type == null) {
            return;
        }

        inventory.setResult(null);
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent event) {
        event.getInventory().setRepairCost((int) Math.ceil(Math.tanh(event.getInventory().getRepairCost()/39.0)*39));
    }

    //TODO: smithing and brewing namespace stored somewhere

    @EventHandler
    public void onSmith(PrepareSmithingEvent event) {
        itc2(event.getInventory(), event.getInventory().getInputMineral());
        itc2(event.getInventory(), event.getInventory().getInputEquipment());
        itc2(event.getInventory(), event.getInventory().getInputTemplate());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        /*System.out.println("sl: "+ event.getSlot());
        System.out.println("rs: " + event.getRawSlot());
        System.out.println("cl: " + event.getClick());
        System.out.println("ci: " + event.getClickedInventory());
        System.out.println("i: " + event.getInventory());
        System.out.println("itemcurse: " + event.getCursor());
        System.out.println("itemcur: " + event.getCurrentItem());
        System.out.println();*/

        if(event.getClick().isShiftClick() && event.getInventory() instanceof BrewerInventory) {
            itc(event, event.getCurrentItem());
        } else if(event.getClickedInventory() instanceof BrewerInventory) {
            itc(event, event.getCursor());
        }
    }

    @EventHandler
    public void onBrewStart(BrewingStartEvent event) {
        ModItem type = NewModPlugin.get().itemManager().getType(event.getSource());

        if (type == null) {
            return;
        }

        //if (!type.isCraftable() && !type.isReplaceableRecipe(new NamespacedKey(NamespacedKey.MINECRAFT, "brewing"))) {
        Bukkit.getScheduler().runTaskLater(NewModPlugin.get(), () -> {
                    BrewingStand stand = (BrewingStand) event.getBlock().getState();

                    stand.getInventory().setIngredient(null);

                    stand.update();
                }, 1);
        //}
    }
}
