package me.bergenfly.newmod.core.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class GeometryUtil {
    public static Vector getRelative(Location reference, Vector local) {
        // Firstly a vector facing YAW = 0, on the XZ plane as start base
        Vector axisBase = new Vector(0, 0, 1);
        // This one pointing YAW + 90° should be the relative "left" of the field of view, isn't it (since ROLL always is 0°)?

        Vector axisLeft = axisBase.clone().rotateAroundY(Math.toRadians(-reference.getYaw() + 90.0f));
        // Left axis should be the rotation axis for going up, too, since it's perpendicular...
        Vector axisUp = reference.getDirection().clone().rotateAroundNonUnitAxis(axisLeft, Math.toRadians(-90f));

        // Based on these directions, we got all we need
        Vector sway = axisLeft.clone().normalize().multiply(local.getX());
        Vector heave = axisUp.clone().normalize().multiply(local.getY());
        Vector surge = reference.getDirection().clone().multiply(local.getZ());

        // Add up the global reference based result
        return new Vector(reference.getX(), reference.getY(), reference.getZ()).add(sway).add(heave).add(surge);
    }
}
