package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.manager.Plots;
import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PermissiblePlotSection;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.plot._1x1_Chunk;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LoadPlot {
    private static final NationsPlugin api = NationsPlugin.getInstance();

    public static Map<Integer, ClaimedChunk> plotsFromMap(YamlConfiguration configuration, File file) {
        Map<Integer, ClaimedChunk> map = new HashMap<>();

        for(String key : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(key);

            int x = section.getInt("x");
            int z = section.getInt("z");
            int id = section.getInt("id");
            int world = section.getInt("world");

            int type = section.getInt("type");
            String administrator = section.getString("administrator");

            LandAdministrator landObject = (LandAdministrator) (api.permissionHoldersByIdRegistry().get(administrator));

            ClaimedChunk chunk = new _1x1_Chunk(x, z, Plots.getWorld(world), landObject);

            landObject.addLand(chunk.getAt(0,0));

            if(chunk.getAt(0,0) instanceof PermissiblePlotSection j) {
                j.loadPermissions(section.getStringList("permissions"));
            }

            map.put(id, chunk);
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
}
