package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;

public class SaveSettlement {
    public static YamlConfiguration settlementToMap(Settlement nation) {
        YamlConfiguration nationSave = new YamlConfiguration();

        nationSave.set("name", nation.getName());
        nationSave.set("leader", nation.getLeader().getUniqueId().toString());
        nationSave.set("firstName", nation.getFirstName());
        nationSave.set("creationTime", nation.getCreationTime());

        return nationSave;
    }

    public static void saveSettlements() {
        for(Settlement settlement : NationsPlugin.getInstance().settlementsRegistry().list()) {
            try {
                settlementToMap(settlement).save(new File("plugins\\Nations\\nations\\" + settlement.getFirstName() + "-" + settlement.getCreationTime() + ".yml"));
            } catch (Exception e) {
                NationsPlugin.getInstance().getLogger().log(Level.SEVERE, "Error trying to save settlement " + settlement.getName() + "(originally " + settlement.getFirstName() + ")");
                e.printStackTrace();
                NationsPlugin.getInstance().getLogger().log(Level.WARNING, "-----------------------------------");
            }
        }
    }
}
