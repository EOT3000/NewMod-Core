package me.bergenfly.nations.model.plot;

import me.bergenfly.nations.model.LandAdministrator;
import me.bergenfly.nations.model.LandOwner;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class Lot {
    private final World world;

    private LandAdministrator administrator;

    private LandOwner owner;

    private int depth;

    private Set<Rectangle> rectangles = new HashSet<>();

    private Set<User> trusted = new HashSet<>();

    public LandAdministrator getAdministrator() {
        return administrator;
    }

    public LandOwner getOwner() {
        return owner;
    }

    public boolean isTrusted(User user) {
        return trusted.contains(user);
    }

    public boolean isAllowed(Level level, PlotPermission permission) {

    }

    public static class Rectangle {
        public final int xMin;
        public final int zMin;
        public final int xMax;
        public final int zMax;

        public Rectangle(int x1, int x2, int z1, int z2) {
            if(x1 < x2) {
                xMin = x1;
                xMax = x2;
            } else {
                xMin = x2;
                xMax = x1;
            }
            if(z1 < z2) {
                zMin = z1;
                zMax = z2;
            } else {
                zMin = z2;
                zMax = z1;
            }
        }

        public Rectangle expanded(int x, int z) {
            return new Rectangle(xMin-x, zMin-z, xMax+x, zMax+z);
        }

        public boolean overlapsWith(Rectangle other) {
            return this.xMin < other.xMax
                    && this.xMax > other.xMin
                    && this.zMax > other.zMin
                    && this.zMin < other.zMax;
        }

        public boolean touches(Rectangle other) {
            return !overlapsWith(other) &&
                    (overlapsWith(other.expanded(1,0)) || overlapsWith(other.expanded(0,1)));
        }
    }
}
