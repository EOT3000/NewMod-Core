package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.manager.Plots;
import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PermissiblePlotSection;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.plot.PlotSectionImpl;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SavePlot {
    public static Map<String, Object> plotToMap(ClaimedChunk chunk) {
        if(chunk.getSections(false).length == 0) {
            return null;
        }

        Map<Integer, Map<String, Object>> sectionsSerialized = new HashMap<>();
        Map<PlotSection, Integer> section2Int = new HashMap<>();

        Map<String, Object> chunkMap = new HashMap<>();

        chunkMap.put("x", chunk.getChunkX());
        chunkMap.put("z", chunk.getChunkZ());
        chunkMap.put("id", Plots.getLocationId(chunk.getChunkX(), chunk.getChunkZ(), chunk.getWorld()));
        chunkMap.put("world", chunk.getWorld().getName());
        chunkMap.put("divisions", chunk.getDivision());

        int count = 0;

        for(PlotSection section : chunk.getSections(true)) {
            Map<String, Object> plot = new HashMap<>();

            if(section == null) {
                plot.put("claimed", "false");
            } else {
                plot.put("claimed", "true");

                if (section instanceof PermissiblePlotSection p) {
                    plot.put("permissions", p.savedPermissionList());
                }

                plot.put("administrator", section.getAdministrator().getId());
            }

            sectionsSerialized.put(count, plot);
            section2Int.put(section, count);
            count++;
        }

        chunkMap.put("sections", sectionsSerialized);

        Map<Object, String> sectionsGeometry = new HashMap<>();

        int num = (int) Math.pow(2, chunk.getDivision());
        int each = 16/num;

        for(int z = 0; z < num; z++) {
            String string = "";

            for (int x = 0; x < num; x++) {
                int xn = x * each;
                int zn = z * each;

                PlotSection section = chunk.getAt(xn, zn);

                Integer id = section2Int.get(section);

                string = string + "," + id;
            }

            string = string.replaceFirst(",", "");

            sectionsGeometry.put(z, string);
        }

        chunkMap.put("geometry", sectionsGeometry);

        return chunkMap;
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
