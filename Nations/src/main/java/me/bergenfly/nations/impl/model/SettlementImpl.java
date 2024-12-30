package me.bergenfly.nations.impl.model;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Community;
import me.bergenfly.nations.api.model.organization.Company;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.plot.PermissiblePlotSectionImpl;
import me.bergenfly.nations.impl.util.IdUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SettlementImpl extends AbstractCommunity implements Settlement {

    private static Registry<Community, String> COMMUNITIES;

    private Set<PlotSection> land = new HashSet<>();

    private final Set<Company> charters = new HashSet<>();

    private SettlementImpl(String name, User leader) {
        this(leader, name, name, System.currentTimeMillis());
    }

    public SettlementImpl(User leader, String name, String firstName, long creationTime) {
        super(leader, name, firstName, creationTime);
    }

    public SettlementImpl(User leader, String name, String firstName, long creationTime, String id) {
        super(leader, name, firstName, creationTime, id);
    }

    public static SettlementImpl tryCreate(String name, User leader) {
        if(COMMUNITIES == null) {
            COMMUNITIES = NationsPlugin.getInstance().communitiesRegistry();
        }

        if(COMMUNITIES.get(name) != null) {
            return null;
        }

        if(leader.getCommunity() != null) {
            return null;
        }

        SettlementImpl s = new SettlementImpl(name, leader);

        COMMUNITIES.set(name, s);

        leader.setCommunity(s);

        return s;
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
    public void sendInfo(CommandSender user) {
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
    public PlotSection createEmptyPlotSection() {
        return new PermissiblePlotSectionImpl(this);
    }

    @Override
    public boolean isUserAdmin(User user) {
        return leader.equals(user);
    }

    //CHARTERING
    @Override
    public void charter(Company company) {
        charters.add(company);
    }

    @Override
    public boolean isChartered(Company company) {
        return charters.contains(company);
    }
}
