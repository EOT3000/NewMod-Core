package me.bergenfly.nations.api.model;

import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
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

    @Nullable Settlement getSettlement();

    /**
     * Sets the user's faction. Also removes itself from the old faction's member list, and adds itself to the new faction member list
     *
     * @param faction the faction that the user will be joining
     */

    void setFaction(Nation faction);

    Collection<Nation> getInvites();
    void addInvite(Nation faction);
    void removeInvite(Nation faction);

    boolean isAdminMode();

    void setAdminMode(boolean adminMode);

    void updateName();

    OfflinePlayer getPlayer();
}
