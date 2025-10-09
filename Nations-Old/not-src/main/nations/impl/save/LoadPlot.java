package me.bergenfly.nations.save;

import me.bergenfly.nations.api.manager.Plots;
import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PermissiblePlotSection;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.model.plot.PermissiblePlotSectionImpl;
import me.bergenfly.nations.model.plot.PlotSectionImpl;
import me.bergenfly.nations.model.plot._1x1_Chunk;
import me.bergenfly.nations.model.plot._2x2_Chunk;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class LoadPlot {
    private static final NationsPlugin api = NationsPlugin.getInstance();

    public static Map<Integer, ClaimedChunk> plotsFromMap(YamlConfiguration configuration, File file) {
        Map<Integer, ClaimedChunk> map = new HashMap<>();

        for(String key : configuration.getKeys(false)) {
            try {
                ConfigurationSection chunkData = configuration.getConfigurationSection(key);

                int x = chunkData.getInt("x");
                int z = chunkData.getInt("z");
                int id = chunkData.getInt("id");
                int world = chunkData.getInt("world");

                int divisions = chunkData.getInt("divisions");

                ClaimedChunk chunk = switch (divisions) {
                    case 0 -> new _1x1_Chunk(x, z, Plots.getWorld(world));
                    case 1 -> new _2x2_Chunk(x, z, Plots.getWorld(world), null, null, null, null);
                    default -> null;
                };

                if(chunk == null) {
                    logError("Error loading chunk with id: " + key + " in file " + file.getName() + " : invalid chunk division " + divisions);
                    continue;
                }

                ConfigurationSection sections = chunkData.getConfigurationSection("sections");

                Map<String, PlotSection> sectionsList = new HashMap<>();

                for (String sectionId : sections.getKeys(false)) {
                    ConfigurationSection section = sections.getConfigurationSection(sectionId);

                    PlotSection plotSection;

                    if(section.getString("claimed", "false").equalsIgnoreCase("false")) {
                        plotSection = null;
                    } else {
                        String administrator = section.getString("administrator");

                        LandAdministrator landObject = (LandAdministrator) (api.permissionHoldersByIdRegistry().get(administrator));

                        boolean hasPermissions = section.contains("permissions");

                        if (hasPermissions) {
                            PermissiblePlotSection pps = new PermissiblePlotSectionImpl(landObject, chunk);

                            pps.loadPermissions(section.getStringList("permissions"));

                            plotSection = pps;
                        } else {
                            plotSection = new PlotSectionImpl(landObject, chunk);
                        }
                    }

                    sectionsList.put(sectionId, plotSection);
                }

                int num = (int) Math.pow(2, divisions);
                int each = 16/num;

                List<Runnable> ifCompletedDo = new ArrayList<>();

                if(divisions == 0 && sectionsList.size() == 1) {
                    PlotSection s = sectionsList.values().iterator().next();

                    chunk.setAt(0, 0, s);

                    s.getAdministrator().addLand(s);
                } else if(divisions == 1 && sectionsList.size() <= 4) {
                    ConfigurationSection rows = chunkData.getConfigurationSection("geometry");

                    for(String row : rows.getKeys(false)) {
                        int rowNumber = Integer.parseInt(row);

                        if(rowNumber > 1) {
                            continue;
                        }

                        String[] columns = rows.getString(row).split(",");

                        for (int xg = 0; xg < 2; xg++) {
                            PlotSection at = sectionsList.get(columns[xg]);

                            chunk.setAt(xg*each, rowNumber*each, at);

                            if(at != null) {
                                ifCompletedDo.add(() -> {
                                    at.getAdministrator().addLand(at);
                                });
                            }
                        }
                    }
                } else {
                    logError("Error loading chunk with id: " + key + " in file " + file.getName() + " : mismatched divisions and section count " + divisions + "," + sectionsList.size());
                }

                for(Runnable runnable : ifCompletedDo) {
                    runnable.run();
                }

                map.put(id, chunk);
            } catch (Exception e) {
                e.printStackTrace();

                logError("Error loading chunk with id: " + key + " in file " + file.getName());
            }
        }

        return map;
    }


    public static void loadPlots() {
        File dir = new File("plugins/Nations/plots");

        if(!dir.exists()) {
            return;
        }

        for(File file : dir.listFiles()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            api.landManager().getPLOTS().addAll(plotsFromMap(config, file));
        }
    }

    private static void logError(String err) {
        api.getLogger().log(Level.SEVERE, err);
    }
}
