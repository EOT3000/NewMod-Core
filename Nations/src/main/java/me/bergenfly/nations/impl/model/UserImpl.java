package me.bergenfly.nations.impl.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserImpl implements User {

    private final UUID uuid;
    private String name;

    private Settlement settlement;

    public UserImpl(UUID uuid) {
        this.uuid = uuid;
        updateName();
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
        return getUniqueId().toString();
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }
}
