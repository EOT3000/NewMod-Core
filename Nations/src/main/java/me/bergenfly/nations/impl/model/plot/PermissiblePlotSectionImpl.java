package me.bergenfly.nations.impl.model.plot;

import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import me.bergenfly.nations.api.model.plot.PermissiblePlotSection;
import me.bergenfly.nations.api.permission.PlotPermission;

import java.util.HashMap;
import java.util.Map;

public class PermissiblePlotSectionImpl extends PlotSectionImpl implements PermissiblePlotSection {
    private final Map<LandPermissionHolder, Object2ByteMap<PlotPermission>> permissions = new HashMap<>();

    public PermissiblePlotSectionImpl(LandAdministrator administrator) {
        super(administrator);
    }

    @Override
    public boolean hasPermission(PlotPermission permission, User user) {
        return false;
    }

    @Override
    public byte getSetPermission(PlotPermission permission, LandPermissionHolder holder) {
        Object2ByteMap<PlotPermission> k = permissions.get(holder);

        return k == null ? 0 : k.getOrDefault(permission, (byte) 0);
    }

    @Override
    public void setPermission(PlotPermission permission, LandPermissionHolder holder, boolean allow) {
        permissions.putIfAbsent(holder, new Object2ByteOpenHashMap<>());

        permissions.get(holder).put(permission, allow ? (byte) 1 : (byte) -1);
    }

    @Override
    public void unsetPermission(PlotPermission permission, LandPermissionHolder holder) {
        permissions.computeIfPresent(holder, (a, b) -> {
            b.put(permission, (byte) 0);

            return b;
        });
    }
}
