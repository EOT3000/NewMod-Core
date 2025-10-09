package me.bergenfly.nations.manager;

import me.bergenfly.nations.model.User;
import me.bergenfly.nations.model.plot.ClaimedChunk;
import org.bukkit.*;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Plots {
    public static void init() {
        boolean worked = false;

        try {
            //TODO optional long storage format
            getXMask = binaryToInteger("0000000000000000 0011111111111111".replaceAll(" ", ""));
            getZMask = binaryToInteger("0000111111111111 1100000000000000".replaceAll(" ", ""));
            getWMask = binaryToInteger("1111000000000000 0000000000000000".replaceAll(" ", ""));

            File file = new File("plugins/Factionals/world-ids.txt");

            Map<Integer, World> to = new HashMap<>();
            Map<World, Integer> from = new HashMap<>();

            if(file.exists()) {
                for(String line : Files.readAllLines(file.toPath())) {
                    String[] spl = line.split(":");

                    String world = spl[0];
                    int id = Integer.parseInt(spl[1]);

                    if(Bukkit.getWorld(world) == null) {
                        continue;
                    }

                    to.put(id, Bukkit.getWorld(world));
                    from.put(Bukkit.getWorld(world), id);

                    worldsTo = to;
                    worldsFrom = from;
                    worked = true;
                }
            } else {
                int count = 0;
                StringBuilder data = new StringBuilder();

                for(World world : Bukkit.getWorlds()) {
                    to.put(count, world);
                    from.put(world, count);
                    data.append(world.getName()).append(":").append(count).append("\n");

                    count++;
                }

                worldsTo = to;
                worldsFrom = from;
                worked = true;

                file.createNewFile();

                try (FileOutputStream stream = new FileOutputStream(file)) {
                    stream.write(data.toString().getBytes(StandardCharsets.UTF_8));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!worked) {
            throw new RuntimeException();
        }
    }


    public static int getXMask;
    public static int getZMask;
    public static int getWMask;

    private static Map<Integer, World> worldsTo;
    private static Map<World, Integer> worldsFrom;


    private static int binaryToInteger(String binary) {
        char[] numbers = binary.toCharArray();
        int result = 0;
        for(int i=numbers.length - 1; i>=0; i--)
            if(numbers[i]=='1')
                result += Math.pow(2, (numbers.length-i - 1));
        return result;
    }

    public static Integer getLocationId(Location location) {
        return getLocationId(location.getChunk());
    }

    public static Integer getLocationId(Chunk chunk) {
        return getLocationId(chunk.getX(), chunk.getZ(), chunk.getWorld());
    }

    public static Integer getLocationId(int x, int z, World world) {
        return getLocationId(x, z, getWorldId(world));
    }

    public static Integer getLocationId(ClaimedChunk chunk) {
        return getLocationId(chunk.getChunkX(), chunk.getChunkZ(), getWorldId(chunk.getWorld()));
    }

    public static Integer getLocationId(int x, int z, int world) {
        return ((x+8192) | ((z+8192) << 14)) | (world << 28);
    }

    public static int getX(int location) {
        return (location & getXMask)-8192;
    }

    public static int getZ(int location) {
        return ((location & getZMask) >> 14)-8192;
    }

    public static int getW(int location) {
        return (location & getWMask) >> 28;
    }

    //TODO: other worlds
    public static int getWorldId(World world) {
        if(world == null) {
            return -1;
        }

        return worldsFrom.getOrDefault(world, -1);
    }

    public static World getWorld(int worldId) {
        if(worldId == -1) {
            return null;
        }

        return worldsTo.getOrDefault(worldId, null);
    }

    /*public static void printChange(Plot plot, String context, String type, String doer) {
        int id = plot.getLocationId();

        Factionals.getFactionals().getLogger().info("{doer}: {context}({type}) {world},{x},{z}"
                .replace("{doer}", doer)
                .replace("{context}", context)
                .replace("{type}", type).replace("{world}", getWorld(getW(id)).getName())
                .replace("{x}", "" + getX(id))
                .replace("{z}", "" + getZ(id)));

        printChange(getWorld(getW(id)), getX(id), getZ(id), context, type, doer);
    }

    public static void printChange(World world, int x, int z, String context, String type, String doer) {
        Factionals.getFactionals().getLogger().info("{doer}: {context}({type}) {world},{x},{z}"
                .replace("{doer}", doer)
                .replace("{context}", context)
                .replace("{type}", type).replace("{world}", world.getName())
                .replace("{x}", "" + x)
                .replace("{z}", "" + z));
    }

    //TODO: Move commands into separate class

    //private static Factionals factionals = Factionals.getFactionals();

    private static void requireNotNull(Object o, String message, CommandSender sender) {
        if(o == null) {
            sender.sendMessage(message);
            throw new CommandRegister.ReturnNowException();
        }
    }

    private static void requirePermission(User user, FactionPermission permission, Faction faction) {
        if(!faction.hasPermission(user, permission)) {
            user.sendMessage(ChatColor.RED + "No permission");
            throw new CommandRegister.ReturnNowException();
        }
    }*/

    /*public static boolean setRegion(CommandSender sender, String a, String region) {
        CommandRegister.requirePlayer(sender);

        User user = factionals.getRegistry(User.class, UUID.class).get(((Player) sender).getUniqueId());
        Plot plot = factionals.getRegistry(Plot.class, Integer.class).get(getLocationId(((Player) sender).getLocation()));

        requireNotNull(plot, ChatColor.RED + "You are not in a plot", sender);
        requirePermission(user, FactionPermission.INTERNAL_MANAGEMENT, plot.getFaction());

        Region factionRegion = plot.getFaction().getRegion(region);

        requireNotNull(factionRegion, ChatColor.RED + "No such region", sender);

        plot.setAdministrator(factionRegion);

        return true;
    }*/
}

