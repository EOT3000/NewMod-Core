package me.bergenfly.newmod.flyfun.technology.util;

import org.bukkit.Location;

/**
 * Class to make accessing energy components simpler.
 */
public record ComponentAccess(Location location, int capacity) {
    public int getCharge() {
        return EnergyUtil.getCharge(location);
    }

    public void setCharge(int charge) {
        EnergyUtil.setCharge(location, charge, capacity);
    }

    public int addCharge(int charge) {
        return EnergyUtil.addCharge(location, charge, capacity);
    }

    public int subtractCharge(int charge) {
        return EnergyUtil.subtractCharge(location, charge, capacity);
    }
}
