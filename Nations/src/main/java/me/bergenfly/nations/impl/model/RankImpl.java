package me.bergenfly.nations.impl.model;

import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Rank;
import me.bergenfly.nations.api.permission.NationPermission;
import me.bergenfly.nations.api.permission.PlotPermission;
import me.bergenfly.nations.impl.util.IdUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RankImpl extends AbstractLedPlayerGroup implements Rank {
    private final Nation nation;
    private final String name;
    private final String id;

    private final long creationTime;

    private RankImpl(String name, Nation nation) {
        this(name, nation, System.currentTimeMillis());
    }

    private RankImpl(String name, Nation nation, long creationTime) {
        this(name, null, nation, creationTime, IdUtil.rankId1(name, nation.getId(), creationTime));
    }

    private RankImpl(String name, User leader, Nation nation, long creationTime, String id) {
        this.nation = nation;
        this.name = name;
        this.id = id;
        this.creationTime = creationTime;

        if(leader != null) {
            setLeader(leader);
        }
    }

    private final Set<NationPermission> permissions = new HashSet<>();

    @Override
    public void setPermission(NationPermission permission) {
        permissions.add(permission);
    }

    @Override
    public void unsetPermission(NationPermission permission) {
        permissions.remove(permission);
    }

    @Override
    public boolean hasPermission(NationPermission permission) {
        return permissions.contains(permission);
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @Nullable Nation getNation() {
        return nation;
    }

    @Override
    public boolean isLandManager(User user) {
        return Rank.super.isLandManager(user);
    }

    @Override
    public int effectivePriority(boolean allowed) {
        return Rank.super.effectivePriority(allowed);
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public void sendInfo(CommandSender user) {

    }
}
