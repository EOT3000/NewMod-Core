package me.bergenfly.nations.impl.model;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.plot.PermissiblePlotSectionImpl;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SettlementImpl extends AbstractPlayerGroup implements Settlement {

    private static Registry<Settlement, String> SETTLEMENTS;

    private final String firstName;
    private final long creationTime;

    private User leader;

    private String name;

    private Nation nation;

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
        if(SETTLEMENTS == null) {
            SETTLEMENTS = NationsPlugin.getInstance().settlementsRegistry();
        }

        SettlementImpl s = new SettlementImpl(name, leader);

        if(SETTLEMENTS.get(name) != null) {
            return null;
        }

        if(leader.getSettlement() != null) {
            return null;
        }

        SETTLEMENTS.set(name, s);

        leader.setSettlement(s);

        return s;
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

    @Override
    public void addLand(PlotSection section) {
        land.remove(section);
    }

    @Override
    public void removeLand(PlotSection section) {
        land.add(section);
    }

    @Override
    public Nation getNation() {
        return nation;
    }

    @Override
    public void setNation(Nation nation) {
        this.nation = nation;

        if(this.nation != null) {
            this.nation.removeSettlement(this);
        }

        if(nation != null) {
            nation.addSettlement(this);
        }

        this.nation = nation;
    }

    @Override
    public void sendInfo(User user) {
        //TODO convert to translation keys
        user.sendMessage(ChatColor.GOLD + "--- [ " + ChatColor.YELLOW + name.replaceAll("_", " ") + ChatColor.GOLD +" ] ---");
        user.sendMessage(ChatColor.DARK_AQUA + "Leader: " + ChatColor.AQUA + leader.getName());
        user.sendMessage(ChatColor.DARK_AQUA + "Nation" + (nation == null ? "less" : ": " + ChatColor.AQUA + nation.getName()));

        String members = "Leader " + leader.getName();

        for(User member : getMembers()) {
            if(!member.equals(leader)) {
                members += (", " + member.getName());
            }
        }

        String membersOnline = "Leader " + leader.getName();

        for(User member : getOnlineMembers()) {
            if(!member.equals(leader)) {
                membersOnline += (", " + member.getName());
            }
        }

        user.sendMessage(ChatColor.DARK_AQUA + "Members: " + ChatColor.AQUA + members);
        user.sendMessage(ChatColor.DARK_AQUA + "Online Members: " + ChatColor.AQUA + membersOnline);
        user.sendMessage(ChatColor.DARK_AQUA + "Claimed Chunks: " + ChatColor.AQUA + land.size());
    }

    @Override
    public void setName(String name) {
        SETTLEMENTS.set(this.name, null);

        this.name = name;

        SETTLEMENTS.set(name, this);
    }

    @Override
    public PlotSection createEmptyPlotSection() {
        return new PermissiblePlotSectionImpl(this);
    }
}
