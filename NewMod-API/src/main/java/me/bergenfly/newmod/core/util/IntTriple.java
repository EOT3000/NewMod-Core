package me.bergenfly.newmod.core.util;

import org.bukkit.Location;

import java.util.Objects;

public class IntTriple extends Triple<Integer, Integer, Integer> {
    public final int x;
    public final int y;
    public final int z;

    public IntTriple(int x, int y, int z) {
        super(x,y,z);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntTriple intTriple = (IntTriple) o;
        return x == intTriple.x && y == intTriple.y && z == intTriple.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public static IntTriple fromLocation(Location location) {
        return new IntTriple(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
