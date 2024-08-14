package me.bergenfly.nations.api.model;

import me.bergenfly.nations.api.model.organization.LandAdministrator;
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
     * Sets the user's settlement. Also removes itself from the old settlement's member list, and adds itself to the new settlement's member list
     *
     * @param settlement the settlement that the user will be joining
     */
    void setSettlement(Settlement settlement);

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
}
