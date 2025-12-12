package me.bergenfly.nations.model;

import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.manager.NationsLandManager;
import me.bergenfly.nations.model.check.Check;
import me.bergenfly.nations.model.plot.ClaimedChunk;
import me.bergenfly.nations.model.plot.PlotSection;
import me.bergenfly.nations.operator.TownOperation;
import me.bergenfly.nations.registry.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class Town implements LandAdministrator {
    private static Registry<Town, String> COMMUNITIES;
    private static NationsLandManager LAND;

    private final Set<User> residents = new HashSet<>();
    private final Set<PlotSection> land = new HashSet<>();
    private final Set<Lot> lots = new HashSet<>();

    private final Set<TownOperation> openProposals = new HashSet<>();

    private final Set<BoardMessage> boardMessages = new HashSet<>();

    private final Set<User> outlaws = new HashSet<>();

    private User leader;
    private String name;

    private ClaimedChunk homePlot;

    private Town(String name, User leader) {
        this.name = name;
        this.leader = leader;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getLeader() {
        return leader;
    }

    public boolean setLeader(User newLeader, boolean silent) {
        if(isResident(newLeader)) {
            this.leader = newLeader;

            return true;
        }

        return false;
    }

    public ClaimedChunk getHomePlot() {
        return homePlot;
    }

    public void setHomePlot(ClaimedChunk homePlot) {
        this.homePlot = homePlot;
    }

    public boolean isResident(User user) {
        return residents.contains(user);
    }

    public void sendInfo(CommandSender sender) {

    }

    public boolean addOutlaw(User toAdd) {
        if(residents.contains(toAdd)) {
            return false;
        }

        outlaws.add(toAdd);
        return true;
    }

    public boolean isOutlaw(User toCheck) {
        return outlaws.contains(toCheck);
    }

    public boolean addResident(User resident, boolean silent) {
        if(Check.checkResidentCanJoinTown(resident, this)) {
            if(resident.hasCommunity()) {
                if (resident.getCommunity() != this) {
                    Town oldCommunity = resident.getCommunity();

                    if (!oldCommunity.removeResident(resident, silent)) {
                        throw new RuntimeException("Unexpected error trying to add resident {1} to town {2}. Unable to kick from old town {3}".replace("{1}", resident.getOfflinePlayer().getName()).replace("{2}", this.getName()).replace("{3}", oldCommunity.getName()));
                    }
                }
            }

            residents.add(resident);

            if(resident.getCommunity() != this) {
                if (!resident.setCommunity(this, silent) || resident.getCommunity() != this) {
                    residents.remove(resident);

                    throw new RuntimeException("Unexpected error trying to add resident {1} to town {2}. Unable to set user's town. User's town is currently {3}".replace("{1}", resident.getOfflinePlayer().getName()).replace("{2}", this.getName()).replace("{3}", String.valueOf(resident.getCommunity())));
                }
            }

            return true;
        }

        return false;
    }

    public boolean canRemoveResident(User resident) {
        return leader != resident;
    }

    public boolean removeResident(User resident, boolean silent) {
        if(canRemoveResident(resident)) {
            residents.remove(resident);

            if(resident.hasCommunity()) {
                resident.setCommunity(null, silent);
            }

            return true;
        }

        return false;
    }


    @Override
    public Set<PlotSection> getLand() {
        return new HashSet<>(land);
    }

    @Override
    public PlotSection createEmptyPlotSection(@NotNull ClaimedChunk in) {
        return null;
    }

    public int getMaxChunks() {
        return residents.size()*20;
    }

    public boolean isSettlement() {
        return true;
    }



    // Messages

    public void broadcast(String string) {
        for(User user : residents) {
            if(user.getOfflinePlayer().isOnline()) {
                user.getPlayer().sendMessage(string);
            }
        }
    }

    public void broadcast(Function<Player, String> stringGenerator) {
        for(User user : residents) {
            if(user.getOfflinePlayer().isOnline()) {
                user.getPlayer().sendMessage(stringGenerator.apply(user.getPlayer()));
            }
        }
    }

    //TODO make this work. Can't use a functional interface as those reset after server restarts
    public void addToBoard(String string) {

    }



    public static ObjectIntPair<Town> tryCreate(String name, User leader, Player player, boolean silent) {
        if(COMMUNITIES == null) {
            COMMUNITIES = NationsPlugin.getInstance().communitiesRegistry();
        }

        if(COMMUNITIES.get(name) != null) {
            return new ObjectIntImmutablePair<>(null, -1);
        }

        if(leader.getCommunity() != null) {
            return new ObjectIntImmutablePair<>(null, -2);
        }

        if(LAND.getClaimedChunkAtLocation(leader.getPlayer().getLocation()) != null) {
            return new ObjectIntImmutablePair<>(null, -3);
        }

        Town s = new Town(name, leader);

        ClaimedChunk homePlot = LAND.tryClaimFullChunkOtherwiseFail(player.getChunk(), s);

        COMMUNITIES.set(name, s);

        leader.setCommunity(s, silent);
        //NationsPlugin.getInstance().permissionManager().registerHolder(s, null);

        return new ObjectIntImmutablePair<>(s, 1);
    }
}
