package me.bergenfly.newmod.flyfun.books.listener;

import me.bergenfly.newmod.flyfun.FlyFunPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;

public class BooksListener implements Listener {

    // The base behaviours - add a book when the paper or bark is in your hand, remove it when it's not, and apply data when it's finished

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHotbarSwitch(PlayerItemHeldEvent event) {
        PlayerInventory inv = event.getPlayer().getInventory();

        ItemStack stack = inv.getItem(event.getNewSlot());

        if (BooksUtils.writable(stack)) {
            BooksUtils.putBook(inv);
        } else if (BooksUtils.isBook(inv.getItemInOffHand())) {
            inv.setItemInOffHand(null);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBookEdit(PlayerEditBookEvent event) {
        if(event.getNewBookMeta().getPersistentDataContainer().getOrDefault(BooksUtils.OFFHAND_ONLY, PersistentDataType.BOOLEAN, false)) {
            PlayerInventory inv = event.getPlayer().getInventory();

            if(!BooksUtils.writable(inv.getItemInMainHand())) {
                Bukkit.getScheduler().runTaskLater(FlyFunPlugin.get(), () -> {
                    if(BooksUtils.isBook(inv.getItemInOffHand())) {
                        inv.setItemInOffHand(null);
                    }
                }, 1);
                return;
            }

            if(inv.getItemInMainHand().getAmount() == 1) {
                BooksUtils.finishWrite(inv, event.getNewBookMeta());
            } else {
                ItemStack add = BooksUtils.finishWriteAdd(inv, event.getNewBookMeta());

                inv.getItemInMainHand().setAmount(inv.getItemInMainHand().getAmount()-1);

                Collection<ItemStack> drop = inv.addItem(add).values();

                if(!drop.isEmpty()) {
                    for(ItemStack stack : drop) {
                        event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), stack);
                    }
                }
            }
        }
    }

    // Other behaviours, in case something goes wrong, or a player tries to dupe items

    @EventHandler
    public void onHandSwitch(PlayerSwapHandItemsEvent event) {
        if(BooksUtils.isBook(event.getMainHandItem()) || BooksUtils.isBook(event.getOffHandItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if(BooksUtils.isBook(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(BooksUtils.isBook(event.getCurrentItem())) {
            if(event.getSlot() == 40 && event.getClickedInventory() instanceof PlayerInventory) {
                event.setCancelled(true);

                Bukkit.getScheduler().runTaskLater(FlyFunPlugin.get(), () -> {
                    if (!BooksUtils.writable(event.getWhoClicked().getInventory().getItemInMainHand())
                            && BooksUtils.isBook(event.getWhoClicked().getInventory().getItemInOffHand())) {
                        event.getWhoClicked().getInventory().setItemInOffHand(null);
                    }
                }, 1);
            }
        } else {
            ItemStack placed = event.getCursor();

            if(BooksUtils.isBook(placed)) {
                if(event.getClickedInventory() == null) {
                    return;
                }

                Bukkit.getScheduler().runTaskLater(FlyFunPlugin.get(), () -> {
                    HumanEntity p = event.getWhoClicked();

                    ItemStack stack = p.getOpenInventory().getItem(event.getRawSlot());

                    if(BooksUtils.isBook(stack)) {
                        p.getOpenInventory().setItem(event.getRawSlot(), null);
                    }

                }, 1);
            }
        }

        if(BooksUtils.isBook(event.getWhoClicked().getInventory().getItemInOffHand())) {
            Bukkit.getScheduler().runTaskLater(FlyFunPlugin.get(), () -> {
                if (!BooksUtils.writable(event.getWhoClicked().getInventory().getItemInMainHand())) {
                    event.getWhoClicked().getInventory().setItemInOffHand(null);
                }
            }, 1);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        //Thank you intellij idea for this beautiful code
        event.getDrops().removeIf(BooksUtils::isBook);
    }

    @EventHandler()
    public void onInteract(PlayerInteractEvent event) {
        if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        if(BooksUtils.signed(event.getItem())) {
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta meta = (BookMeta) book.getItemMeta();

            meta.setPages(BooksUtils.pages(event.getItem()));
            meta.setAuthor("-");
            meta.setTitle("Birch Bark");
            meta.setGeneration(BookMeta.Generation.ORIGINAL);

            book.setItemMeta(meta);

            event.getPlayer().openBook(book);

            /*NMSUtil.sendSetItemPacket(event.getPlayer().getInventory().getHeldItemSlot(), book, event.getPlayer().getInventory());
            Bukkit.getScheduler().runTaskLater(FlyFunPlugin.get(), () -> {
                NMSUtil.openBook(0, event.getPlayer());
            }, 1);*/
        }
    }
}
