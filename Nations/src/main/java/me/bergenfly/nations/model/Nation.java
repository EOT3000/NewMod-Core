package me.bergenfly.nations.model;

import it.unimi.dsi.fastutil.Pair;
import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.manager.NationsLandManager;
import me.bergenfly.nations.model.check.Check;
import me.bergenfly.nations.model.plot.PlotSection;
import me.bergenfly.nations.registry.Registry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Nation implements LandAdministrator {
    private static Registry<Town, String> COMMUNITIES;
    private static Registry<Nation, String> NATIONS;
    private static NationsLandManager LAND;

    private final Set<Town> towns = new HashSet<>();

    private final Set<PlotSection> land = new HashSet<>();

    private final Set<User> outlaws = new HashSet<>();
    private final Set<Town> sanctionedTowns = new HashSet<>();

    private User leader;
    private String name;

    private Town capital;

    private final long creationTime;
    private final String initialName;
    private final String founder;

    public User getLeader() {
        return leader;
    }

    public String getName() {
        return name;
    }

    public Town getCapital() {
        return capital;
    }

    public Set<Town> getSanctionedTowns() {
        return new HashSet<>(sanctionedTowns);
    }

    public Set<User> getOutlaws() {
        return new HashSet<>(outlaws);
    }

    public boolean removeTown(Town town) {
        if(!Check.checkTownCanLeaveNation(town)) {
            return false;
        }

        towns.remove(town);

        if(this.equals(town.getNation())) {
            town.removeFromNation();
        }

        return true;
    }

    public Set<Town> getTowns() {
        return new HashSet<>(towns);
    }

    public Set<User> getResidents() {
        Set<User> ret = new HashSet<>();

        for(Town town : getTowns()) {
            ret.addAll(town.getResidents());
        }

        return ret;
    }

    public boolean addTown(Town town) {
        if(!Check.checkTownCanJoinNation(town, this)) {
            return false;
        }

        if(this.equals(town.getNation())) {
            towns.add(town);

            return true;
        } else if(town.getNation() == null) {
            towns.add(town);
            town.setNation(this);

            return true;
        } else {
            return false;
        }
    }

    public static Pair<Nation, String> tryCreate(Town capital, String name, List<Town> otherTowns) {
        if(NATIONS == null) {
            NATIONS = NationsPlugin.getInstance().nationsRegistry();
        }

        if(NATIONS.get(name) != null) {
            return Pair.of(null, "Fatal error trying to create " + name + "; already exists " + NATIONS.get(name).getName() + " (" + NATIONS.get(name).capital.getName());
        }

        if(capital.getNation() != null) {
            return Pair.of(null, "Fatal error trying to create " + name + "; proposed capital " + capital.getNation() + " is already in nation " + capital.getNation().getName());
        }

        Nation nation = new Nation();

        nation.leader = capital.getLeader();
        nation.name = name;

        for(Town town : otherTowns) {
            if(town.getNation() == null) {
                town.setNation(nation);
            }
        }

        nation.towns.add(capital);

        NATIONS.set(name, nation);

        return Pair.of(nation, "");
    }
}
