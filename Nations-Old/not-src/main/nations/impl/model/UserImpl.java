package me.bergenfly.nations.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Community;
import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
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

    private Community community;

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
        String oldName = this.name;

        this.name = getPlayer().getName();
        api.permissionManager().registerHolder(this, oldName);
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
    public void setCommunity(@Nullable Community community) {
        if(this.community != null) {
            this.community.removeMember(this);
        }

        if(community != null) {
            community.addMember(this);
        }

        this.community = community;
    }

    @Override
    public @Nullable Community getCommunity() {
        return community;
    }

    @Override
    public @Nullable Nation getNation() {
        return community == null ? null : community.getNation();
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

        return api.landManager().tryClaimFullChunkAtLocationOtherwiseFail(location, admin);
    }

    @Override
    public @Nullable PlotSection currentlyAt() {
        if(!isOnline()) {
            return null;
        }

        Location location = getPlayer().getLocation();

        return api.landManager().getPlotSectionAtLocation(location);
    }

    @Override
    public boolean isPartOf(User user) {
        return equals(user);
    }

    @Override
    public int priority() {
        return 0;
    }
}
