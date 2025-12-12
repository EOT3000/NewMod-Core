package me.bergenfly.nations.commands.nation;

import me.bergenfly.nations.model.Nation;
import me.bergenfly.nations.model.Town;

import java.util.ArrayList;
import java.util.List;

public class NationAttempt {
    private final Town capital;
    private final List<Town> agreers = new ArrayList<>();
    private final String name;


    public NationAttempt(Town capital, String name) {
        this.name = name;
        this.capital = capital;
    }

    public void addAgreer(Town settlement) {
        agreers.add(settlement);

        if(agreers.size() > 2) {
            Nation nation = Nation.tryCreate();
        }
    }

    public List<Town> getAgreers() {
        return new ArrayList(agreers);
    }

    public boolean isActive() {
        return true;
    }
}
