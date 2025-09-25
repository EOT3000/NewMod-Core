/*package bergen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Humid extends JavaPlugin {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!sender.hasPermission("humidity")) {
            sender.sendMessage("Added $1000 to your balance");
            return false;
        }

        Block location = ((Player) sender).getLocation().getBlock();

        sender.sendMessage(ChatColor.DARK_GREEN + "" + location.getX() +
                ChatColor.GREEN + "," +
                ChatColor.DARK_GREEN + location.getY() +
                ChatColor.GREEN + "," +
                ChatColor.DARK_GREEN + location.getZ() +
                " in " + ChatColor.YELLOW + location.getWorld());
        sender.sendMessage(ChatColor.GOLD + "Biome: " + ChatColor.YELLOW + location.getBiome());
        sender.sendMessage(ChatColor.DARK_AQUA + "Location Temperature: " + ChatColor.AQUA + location.getTemperature());
        sender.sendMessage(ChatColor.DARK_AQUA + "Location Humidity: " + ChatColor.AQUA + location.getHumidity());

        Biome biome = biome(location.getLocation());

        sender.sendMessage(ChatColor.DARK_AQUA + "Biome Base Temperature: " + ChatColor.AQUA + biome.getBaseTemperature());
        sender.sendMessage(ChatColor.DARK_AQUA + "Biome Base Downfall: " + ChatColor.AQUA + biome.climateSettings.downfall());

        return true;
    }

    private Biome biome(Location location) {
        CraftWorld world = (CraftWorld) (location.getWorld());

        return world.getHandle().getBiome(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ())).value();
    }
}*/
