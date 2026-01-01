package me.bergenfly.nations.model;

import me.bergenfly.nations.config.TownPermission;
import me.bergenfly.nations.model.check.Check;
import me.bergenfly.nations.serializer.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class User implements Serializable {
    private final UUID uuid;

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    private Town community;

    public boolean hasCommunity() {
        return community != null;
    }

    public @Nullable Town getCommunity() {
        return community;
    }

    public boolean isCommunityLeader() {
        return hasCommunity() && this.equals(getCommunity().getLeader());
    }

    public boolean setCommunity(Town newCommunity, boolean silent) {
        if(Check.checkResidentCanJoinTown(this, newCommunity)) {
            if(hasCommunity()) {
                if (getCommunity() != newCommunity) {
                    Town oldCommunity = getCommunity();

                    this.community = null;

                    if(oldCommunity.isResident(this)) {
                        if (!oldCommunity.removeResident(this, silent)) {
                            throw new RuntimeException("Unexpected error trying to set resident {1}'s town to {2}. Unable to kick from old town {3}".replace("{1}", getOfflinePlayer().getName()).replace("{2}", String.valueOf(newCommunity)).replace("{3}", oldCommunity.getName()));
                        }
                    }
                }
            }

            this.community = newCommunity;

            if(newCommunity != null) {
                if(!newCommunity.isResident(this)) {
                    if (!newCommunity.addResident(this, false) || !newCommunity.isResident(this)) {
                        this.community = null;

                        throw new RuntimeException("Unexpected error trying to set resident {1}'s town to {2}. Unable to add user to town".replace("{1}", getOfflinePlayer().getName()).replace("{2}", newCommunity.getName()));
                    }
                }
            }

            return true;
        }

        return false;
    }

    public @NotNull OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean hasTownPermission(TownPermission permission) {
        return false;
    }

    @Override
    public Object serialize() {
        return getId();
    }

    @Override
    public String getId() {
        return uuid.toString();
    }
}
