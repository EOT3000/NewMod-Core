package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SaveNation {
    public static YamlConfiguration nationToMap(Nation nation) {
        YamlConfiguration nationSave = new YamlConfiguration();

        nationSave.set("name", nation.getName());
        nationSave.set("leader", nation.getLeader().getUniqueId().toString());
        nationSave.set("capital", nation.getCapital().getName());
        nationSave.set("firstName", nation.getFirstName());
        nationSave.set("creationTime", nation.getCreationTime());
        nationSave.set("settlements", nation.getSettlements().stream().map(Settlement::getName).collect(Collectors.toSet()));

        return nationSave;
    }

    public static void saveNations() {
        for(Nation nation : NationsPlugin.getInstance().nationsRegistry().list()) {
            try {
                nationToMap(nation).save(new File("plugins\\Nations\\nations\\" + nation.getFirstName() + "-" + nation.getCreationTime() + ".yml"));
            } catch (Exception e) {
                NationsPlugin.getInstance().getLogger().log(Level.SEVERE, "Error trying to save nation " + nation.getName() + "(originally " + nation.getFirstName() + ")");
                e.printStackTrace();
                NationsPlugin.getInstance().getLogger().log(Level.WARNING, "-----------------------------------");
            }
        }
    }
}
