package me.bergenfly.nations.impl.save;

import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.impl.model.NationImpl;
import org.bukkit.configuration.file.YamlConfiguration;

public class LoadNation {
    public static Nation mapToNation(YamlConfiguration configuration) {
        Nation nation = new NationImpl(configuration.getName())
    }
}
