package me.bergenfly.nations.config;


import it.unimi.dsi.fastutil.objects.Object2BooleanMap;

public class TownRank {
    private static Object2BooleanMap<TownPermission> permissions;

    public boolean hasPermission(TownPermission permission) {
        return permissions.getBoolean(permission);
    }
}
