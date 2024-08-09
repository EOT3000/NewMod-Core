package me.bergenfly.nations.impl.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Settlement;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SettlementImpl implements Settlement {

    private final String firstName;
    private final long creationTime;

    private String name;

    public SettlementImpl(String name) {
        this(name, name, System.currentTimeMillis());
    }

    public SettlementImpl(String name, String firstName, long creationTime) {
        this.name = name;
        this.firstName = firstName;
        this.creationTime = creationTime;
    }

    @Override
    public @NotNull String getId() {
        return "settlement_" + firstName.toLowerCase() + "_" + creationTime;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void broadcastString(String s) {

    }

    @Override
    public Set<User> getMembers() {
        return Set.of();
    }

    @Override
    public Set<User> getOnlineMembers() {
        return Set.of();
    }

    @Override
    public boolean register() {
        return false;
    }
}
