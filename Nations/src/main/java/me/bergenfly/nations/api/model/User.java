package me.bergenfly.nations.api.model;

import me.bergenfly.nations.api.model.organization.*;
import me.bergenfly.nations.api.model.plot.PlotSection;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

//TODO: all newmod plugins single user class?

/**
 * An object which represents a player, with methods specific to the Nations plugin.
 */
public interface User extends LandPermissionHolder {
    @NotNull UUID getUniqueId();

    void sendMessage(String s);

    @Nullable Nation getNation();

    @Nullable Community getCommunity();

    /**
     * Sets the user's community. Also removes itself from the old community's member list, and adds itself to the new community's member list
     *
     * @param community the community that the user will be joining
     */
    void setCommunity(Community community);

    Collection<Nation> getInvites();
    void addInvite(Nation faction);
    void removeInvite(Nation faction);

    boolean isAdminMode();

    void setAdminMode(boolean adminMode);

    void updateName();

    OfflinePlayer getPlayer();

    default boolean isOnline() {
        return getPlayer().isOnline();
    }

    boolean tryClaimChunk(LandAdministrator admin);

    @Nullable PlotSection currentlyAt();
}
