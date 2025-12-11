package me.bergenfly.nations.model;

import me.bergenfly.nations.manager.NationsLandManager;
import me.bergenfly.nations.model.plot.PlotSection;
import me.bergenfly.nations.registry.Registry;

import java.util.HashSet;
import java.util.Set;

public class Nation implements LandAdministrator {
    private static Registry<Town, String> COMMUNITIES;
    private static NationsLandManager LAND;

    private final Set<User> residents = new HashSet<>();
    private final Set<PlotSection> land = new HashSet<>();

    private final Set<User> outlaws = new HashSet<>();

    private User leader;
    private String name;

    private Town capital;



    public static Nation tryCreate() {

    }
}
