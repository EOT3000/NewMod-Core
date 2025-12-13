package me.bergenfly.nations.commands.nation;

import it.unimi.dsi.fastutil.Pair;
import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.command.TranslatableString;
import me.bergenfly.nations.model.Nation;
import me.bergenfly.nations.model.Town;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class NationAttempt {
    private final Town capital;
    private final List<Town> agreers = new ArrayList<>();
    private final String name;
    private final List<Town> others;

    private boolean active = true;

    public NationAttempt(Town capital, String name, Town... others) {
        this.name = name;
        this.capital = capital;

        this.others = Arrays.asList(others);
    }

    public void addAgreer(Town settlement) {
        if(!others.contains(settlement)) {
            return;
        }

        agreers.add(settlement);

        if(agreers.size() > 2) {
            Pair<Nation, String> nationResult = Nation.tryCreate(capital, name, others);

            if(nationResult.first() == null) {
                NationsPlugin.getInstance().addReminder(capital.getLeader().getOfflinePlayer().getUniqueId(), TranslatableString.translate("nations.admin_help", nationResult.second()));
            }
        }
    }

    public List<Town> getAgreers() {
        return new ArrayList(agreers);
    }

    public boolean canJoin(Town town) {
        return others.contains(town);
    }

    public boolean isActive() {
        return active;
    }
}
