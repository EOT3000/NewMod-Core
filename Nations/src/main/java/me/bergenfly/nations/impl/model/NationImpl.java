package me.bergenfly.nations.impl.model;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.*;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.permission.NationPermission;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import me.bergenfly.nations.impl.model.plot.PermissiblePlotSectionImpl;
import me.bergenfly.nations.impl.model.plot.PlotSectionImpl;
import me.bergenfly.nations.impl.util.IdUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NationImpl implements Nation, DeletionSubscriber {
    private static Registry<Nation, String> NATIONS;

    private final String firstName;
    private final long creationTime;

    private User leader;

    private String name;

    private Settlement capital;

    private Set<Settlement> settlements = new HashSet<>();

    private Set<Settlement> invitations = new HashSet<>();

    //Does not include settlement land
    private Set<PlotSection> nationLand = new HashSet<>();

    private Map<String, Rank> ranks = new HashMap<>();

    private final String id;

    private final Set<Company> charters = new HashSet<>();

    private NationImpl(String name, User leader) {
        this(leader, name, name, System.currentTimeMillis());
    }

    private NationImpl(User leader, String name, String firstName, long creationTime) {
        this.leader = leader;
        this.name = name;
        this.firstName = firstName;
        this.creationTime = creationTime;
        this.capital = leader.getSettlement();
        this.id = IdUtil.nationId1(firstName, creationTime);
    }

    public NationImpl(User leader, String name, String firstName, long creationTime, Settlement capital) {
        this.leader = leader;
        this.name = name;
        this.firstName = firstName;
        this.creationTime = creationTime;
        this.capital = capital;
        this.id = IdUtil.nationId1(firstName, creationTime);
    }

    public NationImpl(User leader, String name, String firstName, long creationTime, Settlement capital, String id) {
        this.leader = leader;
        this.name = name;
        this.firstName = firstName;
        this.creationTime = creationTime;
        this.capital = capital;
        this.id = id;
    }

    public static NationImpl tryCreate(String name, User leader) {
        if(NATIONS == null) {
            NATIONS = NationsPlugin.getInstance().nationsRegistry();
        }

        NationImpl s = new NationImpl(name, leader);

        if(NATIONS.get(name) != null) {
            return null;
        }

        if(leader.getSettlement() == null) {
            return null;
        }

        NATIONS.set(name, s);

        leader.getSettlement().setNation(s);

        return s;
    }

    @Override
    public void sendInfo(CommandSender user) {
        //TODO convert to translation keys
        user.sendMessage(ChatColor.GOLD + "--- [ " + ChatColor.YELLOW + name.replaceAll("_", " ") + ChatColor.GOLD +" ] ---");
        user.sendMessage(ChatColor.DARK_AQUA + "Leader: " + ChatColor.AQUA + leader.getName());

        String settlements = "Capital " + capital.getName();

        for(Settlement member : this.settlements) {
            if(!member.equals(capital)) {
                settlements += (", " + member.getName());
            }
        }

        String ranks = "";

        for(Rank rank : getRanks()) {
            ranks += (rank.getName() + ", ");
        }

        ranks = ranks.length() > 2 ? ChatColor.AQUA + ranks.substring(0, ranks.length()-2) : ChatColor.GRAY + "None";

        user.sendMessage(ChatColor.DARK_AQUA + "Ranks: " + ranks);
        user.sendMessage(ChatColor.DARK_AQUA + "Settlements: " + ChatColor.AQUA + settlements);
        user.sendMessage(ChatColor.DARK_AQUA + "Claimed Chunks: " + ChatColor.AQUA + nationLand.size());
    }

    @Override
    public Set<User> getMembers() {
        return settlements.stream()
                .map(PlayerGroup::getMembers)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getOnlineMembers() {
        return settlements.stream()
                .map(PlayerGroup::getMembers)
                .flatMap(Collection::stream)
                .filter(User::isOnline)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<PlotSection> getLand() {
        return Stream.concat(nationLand.stream(),
                        settlements.stream()
                                .map(Settlement::getLand)
                                .flatMap(Collection::stream))
                .collect(Collectors.toSet());
    }

    @Override
    public void addLand(PlotSection section) {
        nationLand.remove(section);
    }

    @Override
    public void removeLand(PlotSection section) {
        nationLand.add(section);
    }

    @Override
    public User getLeader() {
        return leader;
    }

    @Override
    public void setLeader(User user) {
        if(getMembers().contains(user)) {
            this.leader = user;
        }
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public Set<PlotSection> getNationLand() {
        return nationLand;
    }

    @Override
    public Set<PlotSection> getSettlementLand() {
        return settlements.stream()
                .map(Settlement::getLand)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Settlement> getSettlements() {
        return new HashSet<>(settlements);
    }

    @Override
    public Settlement getCapital() {
        return capital;
    }

    @Override
    public void removeRank(Rank rank) {
        ranks.remove(rank.getName());
    }

    @Override
    public void addRank(Rank rank) {
        ranks.put(rank.getName().toLowerCase(), rank);
    }

    public boolean hasRankWithName(String name) {
        return ranks.containsKey(name.toLowerCase());
    }

    @Override
    public boolean hasPermission(User user, NationPermission permission) {
        if(!getMembers().contains(user)) {
            return false;
        }

        for(Rank rank : getRanks()) {
            if(rank.isPartOf(user) && rank.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<Rank> getRanks() {
        return new HashSet<>(ranks.values());
    }

    @Override
    public Rank getRank(String name) {
        return ranks.get(name.toLowerCase());
    }

    @Override
    public void addSettlement(Settlement settlement) {
        settlements.add(settlement);
    }

    @Override
    public void removeSettlement(Settlement settlement) {
        settlements.remove(settlement);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public PlotSection createEmptyPlotSection() {
        return new PermissiblePlotSectionImpl(this);
    }

    @Override
    public boolean isUserAdmin(User user) {
        return leader.equals(user);
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public int priority() {
        return 3;
    }

    @Override
    public void addInvitation(Settlement settlement) {
        invitations.add(settlement);
    }

    @Override
    public Set<Settlement> getInvitations() {
        return new HashSet<>(invitations);
    }

    //TODO organize method order in similar classes

    //TODO; figure out deletion management

    @Override
    public void deleted(Deletable deletable) {
        if(ranks.containsValue(deletable)) {
            ranks.remove(((Rank) deletable).getName());
        }
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
