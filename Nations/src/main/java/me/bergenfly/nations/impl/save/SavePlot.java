package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.manager.Plots;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SavePlot {
    public static Map<String, Object> plotToMap(ClaimedChunk chunk) {
        if(chunk.getAt(0, 0) == null) {
            return null;
        }

        Map<String, Object> plot = new HashMap<>();

        plot.put("x", chunk.getChunkX());
        plot.put("z", chunk.getChunkZ());
        plot.put("id", Plots.getLocationId(chunk.getChunkX(), chunk.getChunkZ(), chunk.getWorld()));
        plot.put("world", chunk.getWorld().getName());

        plot.put("type", chunk.getDivision());
        plot.put("administrator", chunk.getAt(0, 0).getAdministrator().getId());

        return plot;
    }

    public static YamlConfiguration plotsToMap(List<ClaimedChunk> chunks) {
        YamlConfiguration plots = new YamlConfiguration();

        for(ClaimedChunk chunk : chunks) {
            Map<String, Object> plot = plotToMap(chunk);

            plots.set(Plots.getLocationId(chunk.getChunkX(), chunk.getChunkZ(), chunk.getWorld()).toString(), plot);
        }

        return plots;
    }

    public static void savePlots() {
        List<ClaimedChunk> allChunks = NationsPlugin.getInstance().landManager().getPLOTS().list();

        Collection<List<ClaimedChunk>> byWorld = new ArrayList<>();

        while(!allChunks.isEmpty()) {
            World world = allChunks.getFirst().getWorld();

            Map<Boolean, List<ClaimedChunk>> map = allChunks.stream().collect(Collectors.partitioningBy((a) -> world.equals(a.getWorld())));

            byWorld.add(map.get(true));

            allChunks = map.get(false);
        }

        for(List<ClaimedChunk> list : byWorld) {
            try {
                plotsToMap(list).save(new File("plugins/Nations/plots/" + list.getFirst().getWorld().getName() + ".yml"));
            } catch (Exception e) {
                NationsPlugin.getInstance().getLogger().log(Level.SEVERE, "Error trying to save plots in world " + list.getFirst().getWorld());
                e.printStackTrace();
                NationsPlugin.getInstance().getLogger().log(Level.WARNING, "-----------------------------------");
            }
        }
    }
}
