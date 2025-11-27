package me.bergenfly.nations.model;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.manager.NationsLandManager;
import me.bergenfly.nations.model.check.Check;
import me.bergenfly.nations.model.plot.ClaimedChunk;
import me.bergenfly.nations.model.plot.PlotSection;
import me.bergenfly.nations.registry.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Settlement implements LandAdministrator {
    private static Registry<Settlement, String> COMMUNITIES;
    private static NationsLandManager LAND;

    private final Set<User> residents = new HashSet<>();
    private final Set<PlotSection> land = new HashSet<>();
    private final Set<Lot> lots = new HashSet<>();

    private User leader;
    private String name;

    private ClaimedChunk homePlot;

    private Settlement(String name, User leader) {
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

    public boolean addResident(User resident, boolean silent) {
        if(Check.checkResidentCanJoinTown(resident, this)) {
            if(resident.hasCommunity()) {
                if (resident.getCommunity() != this) {
                    Settlement oldCommunity = resident.getCommunity();

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

    public static ObjectIntPair<Settlement> tryCreate(String name, User leader, Player player, boolean silent) {
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

        Settlement s = new Settlement(name, leader);

        ClaimedChunk homePlot = LAND.tryClaimFullChunkOtherwiseFail(player.getChunk(), s);

        COMMUNITIES.set(name, s);

        leader.setCommunity(s, silent);
        //NationsPlugin.getInstance().permissionManager().registerHolder(s, null);

        return new ObjectIntImmutablePair<>(s, 1);
    }
}
