package me.bergenfly.nations.impl.model;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SettlementImpl extends AbstractPlayerGroup implements Settlement {

    private final String firstName;
    private final long creationTime;

    private User leader;

    private String name;

    private Set<PlotSection> land = new HashSet<>();

    private SettlementImpl(String name, User leader) {
        this(leader, name, name, System.currentTimeMillis());
    }

    private SettlementImpl(User leader, String name, String firstName, long creationTime) {
        this.leader = leader;
        this.name = name;
        this.firstName = firstName;
        this.creationTime = creationTime;
    }

    public static SettlementImpl tryCreate(String name, User leader) {
        //TODO checks and register

        return new SettlementImpl(name, leader);
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
    public boolean register() {
        return false;
    }

    @Override
    public User getLeader() {
        return leader;
    }

    @Override
    public void setLeader(User leader) {
        this.leader = leader;
    }

    @Override
    public Set<PlotSection> getLand() {
        return new HashSet<>(land);
    }
}
