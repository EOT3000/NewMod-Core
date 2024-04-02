/*package me.fly.newmod.core.util;

import net.minecraft.network.protocol.game.PacketPlayOutOpenBook;
import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.EnumHand;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class NMSUtil {
    static {
        
    }

    public static int getSkyBrightness(World world) {
        return ((CraftWorld) world).getHandle().C_();
    }

    /*public static void sendSetItemPacket(int index, ItemStack item, PlayerInventory inv) {
        if (inv.getHolder() != null) {
            EntityPlayer player = ((CraftPlayer)inv.getHolder()).getHandle();

            if (player.c != null) {
                if (index < net.minecraft.world.entity.player.PlayerInventory.g()) {
                    index += 36;
                } else if (index > 39) {
                    index += 5;
                } else if (index > 35) {
                    index = 8 - (index - 36);
                }

                player.c.a(new PacketPlayOutSetSlot(player.bQ.j, player.bQ.j() + 1 & 32767, index, CraftItemStack.asNMSCopy(item)));
            }
        }
    }/

    public static void openBook(int hand, Player player) {
        PacketPlayOutOpenBook book = new PacketPlayOutOpenBook(EnumHand.values()[hand]);

        ((CraftPlayer) player).getHandle().c.a(book);
    }
}*/
