package me.bergenfly.nations.model.plot;

import me.bergenfly.nations.config.DefaultTownPermission;
import me.bergenfly.nations.config.TownPermission;
import me.bergenfly.nations.model.LandAdministrator;
import me.bergenfly.nations.model.LandOwner;
import me.bergenfly.nations.model.User;
import me.bergenfly.nations.serializer.IdList;
import me.bergenfly.nations.serializer.Serializable;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class Lot implements Serializable {
    private final World world;

    private LandAdministrator administrator;

    private LandOwner owner;

    private int depth;

    private Set<Rectangle> rectangles = new HashSet<>();

    private Set<User> trusted = new HashSet<>();

    private Map<PlotPermission, List<Level>> setPermissions = new HashMap<>();

    public LandAdministrator getAdministrator() {
        return administrator;
    }

    public LandOwner getOwner() {
        return owner;
    }


    public LandOwner getOwnerOrTenant(Location location) {
        return getOwner();
    }

    public boolean isTrusted(User user) {
        return trusted.contains(user);
    }



    public boolean isAllowed(User user, PlotPermission permission, Location location) {
        if(location.getBlockZ() >= depth) {
            return isTrusted(user) || (setPermissions.containsKey(permission) && belongsTo(setPermissions.get(permission), user));
        } else {
            return administrator.equals(user.getCommunity()) && user.hasTownPermission(DefaultTownPermission.ACCESS_SUBWAY);
        }
    }

    private boolean belongsTo(List<Level> levels, User user) {
        if(levels.contains(Level.EVERYONE)) {
            return true;
        }

        return false;
    }

    public static class Rectangle implements Serializable {
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

        @Override
        public String serialize() {
            return xMin+","+zMin+";"+xMax+","+zMax;
        }

        @Override
        public String getId() {
            throw new UnsupportedOperationException("Rectangle does not support IDs");
        }
    }

    @Override
    public String getId() {
        throw new UnsupportedOperationException("Lot does not support IDs (yet?)");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();

        ret.put("rectangles", rectangles);
        ret.put("world", world.getName());
        ret.put("administrator", administrator.getId());
        ret.put("owner", owner.getId());
        ret.put("trusted", new IdList(trusted));

        return null;
    }
}
