package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SaveSettlement {
    public static YamlConfiguration settlementToMap(Settlement settlement) {
        YamlConfiguration settlementSave = new YamlConfiguration();

        settlementSave.set("name", settlement.getName());
        settlementSave.set("id", settlement.getId());
        settlementSave.set("leader", settlement.getLeader().getUniqueId().toString());
        if(settlement.getFirstName() != null) settlementSave.set("firstName", settlement.getFirstName());
        if(settlement.getCreationTime() != -1) settlementSave.set("creationTime", settlement.getCreationTime());
        settlementSave.set("members", settlement.getMembers().stream().filter(Objects::nonNull).map(User::getUniqueId).map(UUID::toString).collect(Collectors.toList()));

        return settlementSave;
    }

    public static void saveSettlements() {
        for(Settlement settlement : NationsPlugin.getInstance().settlementsRegistry().list()) {
            try {
                settlementToMap(settlement).save(new File("plugins/Nations/settlements/" + settlement.getId() + ".yml"));
            } catch (Exception e) {
                NationsPlugin.getInstance().getLogger().log(Level.SEVERE, "Error trying to save settlement " + settlement.getName() + "(originally " + settlement.getFirstName() + ")");
                e.printStackTrace();
                NationsPlugin.getInstance().getLogger().log(Level.WARNING, "-----------------------------------");
            }
        }
    }
}
