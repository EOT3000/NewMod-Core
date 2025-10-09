package me.bergenfly.nations.model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class User {
    private final UUID uuid;

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    private Settlement community;

    public Settlement getCommunity() {
        return community;
    }

    public void setCommunity(Settlement community) {
        this.community = community;
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
