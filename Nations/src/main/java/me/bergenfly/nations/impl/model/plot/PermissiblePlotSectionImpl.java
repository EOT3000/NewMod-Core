package me.bergenfly.nations.impl.model.plot;

import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PermissiblePlotSection;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.permission.DefaultPlotPermission;
import me.bergenfly.nations.api.permission.PlotPermission;
import me.bergenfly.nations.impl.NationsPlugin;

import java.util.*;

public class PermissiblePlotSectionImpl extends PlotSectionImpl implements PermissiblePlotSection {
    private final Map<LandPermissionHolder, Object2ByteMap<PlotPermission>> permissions = new HashMap<>();

    private LandPermissionHolder owner;

    private static final NationsPlugin api = NationsPlugin.getInstance();

    private boolean claimable = false;

    public PermissiblePlotSectionImpl(LandAdministrator administrator, ClaimedChunk in) {
        super(administrator, in);

        this.owner = administrator;
    }

    @Override
    public boolean hasPermission(PlotPermission permission, User user) {
        if(owner.isOwnedLandManager(user)) return true;

        //Tested this: pretty but slow
        OptionalInt i = permissions.keySet().stream()
                .filter((a) -> permissions.get(a).getOrDefault(permission, (byte) 0) != 0)
                .filter((a) -> a.isPartOf(user))
                .mapToInt((a) -> a.effectivePriority(permissions.get(a).getByte(permission) >= 1))
                .min();

        return i.isPresent() && i.getAsInt() % 2 == 1;
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

    //TODO should be set or collection?
    @Override
    public List<String> savedPermissionList() {
        List<String> list = new ArrayList<>();

        for(LandPermissionHolder holder : permissions.keySet()) {
            Object2ByteMap<PlotPermission> map = permissions.get(holder);

            for(PlotPermission permission : map.keySet()) {
                if(map.getOrDefault(permission, (byte) 0) != 0) {
                    list.add(holder.getId() + ";" + permission.getKey() + ";" + (map.getByte(permission) > 0));
                }
            }
        }

        return list;
    }

    @Override
    public void loadPermissions(List<String> list) {
        for(String s : list) {
            String[] spl = s.split(";");

            LandPermissionHolder holder = api.permissionHoldersByIdRegistry().get(spl[0]);

            permissions.putIfAbsent(holder, new Object2ByteOpenHashMap<>());

            permissions.get(holder).put(DefaultPlotPermission.of(spl[1]), Boolean.valueOf(spl[2]) ? (byte) 1 : (byte) 0);
        }
    }

    @Override
    public LandPermissionHolder getOwner() {
        return owner;
    }

    @Override
    public void setOwner(LandPermissionHolder owner) {
        this.owner = owner;
        this.setClaimable(false);
    }

    @Override
    public boolean isClaimable() {
        return claimable;
    }

    @Override
    public void setClaimable(boolean claimable) {
        this.claimable = claimable;
    }

    @Override
    public PermissiblePlotSection cloneAt(ClaimedChunk claimedChunk) {
        PermissiblePlotSectionImpl section = new PermissiblePlotSectionImpl(getAdministrator(), claimedChunk);

        section.setOwner(owner);

        for(LandPermissionHolder holder : permissions.keySet()) {
            section.permissions.put(holder, new Object2ByteOpenHashMap<>(permissions.get(holder)));
        }

        section.claimable = claimable;

        return section;
    }
}
