package me.bergenfly.nations.impl.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserImpl implements User {

    private static NationsPlugin api = NationsPlugin.getInstance();

    private final UUID uuid;
    private String name;

    private Settlement settlement;

    public UserImpl(UUID uuid) {
        this.uuid = uuid;
        updateName();
    }

    public UserImpl(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }

    @Override
    public void updateName() {
        this.name = getPlayer().getName();
    }

    @Override
    public void setAdminMode(boolean adminMode) {
        //TODO
    }

    @Override
    public boolean isAdminMode() {
        return false;
    }

    @Override
    public void removeInvite(Nation faction) {
        //TODO and replace with settlement not faction
    }

    @Override
    public void addInvite(Nation faction) {

    }

    @Override
    public Collection<Nation> getInvites() {
        return List.of();
    }

    @Override
    public void setSettlement(@Nullable Settlement settlement) {
        if(this.settlement != null) {
            this.settlement.removeMember(this);
        }

        if(settlement != null) {
            settlement.addMember(this);
        }

        this.settlement = settlement;
    }

    @Override
    public @Nullable Settlement getSettlement() {
        return settlement;
    }

    @Override
    public @Nullable Nation getNation() {
        return settlement == null ? null : settlement.getNation();
    }

    @Override
    public void sendMessage(String s) {
        if(getPlayer().isOnline()) {
            //noinspection DataFlowIssue
            getPlayer().getPlayer().sendMessage(s);
        }
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public @NotNull String getId() {
        return "user_" + getUniqueId();
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public boolean tryClaimChunk(LandAdministrator admin) {
        if(!isOnline()) {
            return false;
        }

        Location location = getPlayer().getLocation();

        return api.landManager().tryClaimChunkAtLocation(location, admin);
    }

    @Override
    public int priority() {
        return 0;
    }
}
