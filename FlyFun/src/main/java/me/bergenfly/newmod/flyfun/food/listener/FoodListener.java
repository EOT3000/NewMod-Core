package me.bergenfly.newmod.flyfun.food.listener;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import me.bergenfly.newmod.core.api.NewModAPI;
import me.bergenfly.newmod.core.api.block.BlockManager;
import me.bergenfly.newmod.core.api.blockstorage.BlockStorage;
import me.bergenfly.newmod.core.api.item.ItemManager;
import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import me.bergenfly.newmod.flyfun.food.nutrient.Food;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class FoodListener implements Listener {
    private static final FlyFunPlugin plugin = FlyFunPlugin.get();
    private static final NewModAPI api = plugin.api;
    private static final BlockStorage block = api.blockStorage();
    private static final BlockManager blockManager = api.blockManager();
    private static final ItemManager item = api.itemManager();

    public static final NamespacedKey CUSTOM_FOOD = new NamespacedKey(plugin, "custom_food");

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event) {
        ItemStack stack = tryFixFood(event.getItem().getItemStack());

        if(stack != null) {
            event.getItem().setItemStack(stack);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        System.out.println(event.getPlayer());

        for(int i = 0; i < event.getInventory().getSize(); i++) {
            ItemStack old = event.getInventory().getItem(i);

            if(old == null) {
                continue;
            }

            ItemStack stack = tryFixFood(old);

            if (stack != null) {
                event.getInventory().setItem(i, stack);
            }
        }

        for(int i = 0; i < event.getView().getBottomInventory().getSize(); i++) {
            ItemStack old = event.getView().getBottomInventory().getItem(i);

            if(old == null) {
                continue;
            }

            ItemStack stack = tryFixFood(old);

            if (stack != null) {
                event.getView().getBottomInventory().setItem(i, stack);
            }
        }
    }

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        ItemStack oldMain = event.getMainHandItem();

        ItemStack stackMain = tryFixFood(oldMain);

        if (stackMain != null) {
            event.setMainHandItem(stackMain);
        }

        ItemStack oldOff = event.getOffHandItem();

        ItemStack stackOff = tryFixFood(oldOff);

        if (stackOff != null) {
            event.setOffHandItem(stackOff);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChangeHand(PlayerItemHeldEvent event) {
        ItemStack old = event.getPlayer().getInventory().getItemInMainHand();

        ItemStack stackMain = tryFixFood(old);

        if (stackMain != null) {
            event.getPlayer().getInventory().setItemInMainHand(stackMain);
        }
    }

    public static ItemStack tryFixFood(ItemStack stack) {
        Food food = Food.getFood(item.getType(stack));

        String s = stack.getPersistentDataContainer().get(CUSTOM_FOOD, PersistentDataType.STRING);

        if(food != null && s == null) {
            FoodProperties foodProperties = stack.getData(DataComponentTypes.FOOD);
            FoodProperties defaultFoodProperties = stack.getType().getDefaultData(DataComponentTypes.FOOD);

            Consumable consumableProperties = stack.getData(DataComponentTypes.CONSUMABLE);
            Consumable defaultConsumableProperties = stack.getType().getDefaultData(DataComponentTypes.CONSUMABLE);

            boolean foodMatches;
            boolean consMatches;

            if(foodProperties == null) {
                foodMatches = defaultFoodProperties == null;
            } else {
                foodMatches = foodProperties.equals(defaultFoodProperties);
            }

            if(consumableProperties == null) {
                consMatches = defaultConsumableProperties == null;
            } else {
                consMatches = consumableProperties.equals(defaultConsumableProperties);
            }

            if(foodMatches && consMatches) {
                FoodProperties newFP = FoodProperties.food()
                        .nutrition(food.getNutrition())
                        .saturation(food.getSaturation())
                        .build();

                Consumable newC = Consumable.consumable()
                        .consumeSeconds(food.consumeTimeSeconds()).
                        build();

                stack.setData(DataComponentTypes.FOOD, newFP);
                stack.setData(DataComponentTypes.CONSUMABLE, newC);

                ItemMeta meta = stack.getItemMeta();

                meta.getPersistentDataContainer().set(CUSTOM_FOOD, PersistentDataType.STRING, "set");

                stack.setItemMeta(meta);

                return stack;
            }
        }

        return null;
    }
}
