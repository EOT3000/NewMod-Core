package me.bergenfly.nations.model;

import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.manager.NationsLandManager;
import me.bergenfly.nations.model.check.Check;
import me.bergenfly.nations.model.plot.ClaimedChunk;
import me.bergenfly.nations.model.plot.PlotSection;
import me.bergenfly.nations.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Settlement implements LandAdministrator {
    private static Registry<Settlement, String> COMMUNITIES;
    private static NationsLandManager LAND;

    private Set<User> residents = new HashSet<>();

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

    public void setLeader(User leader) {
        this.leader = leader;
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
        return Set.of();
    }

    @Override
    public void removeLand(PlotSection section) {

    }

    @Override
    public PlotSection createEmptyPlotSection(@Nullable ClaimedChunk in) {
        return null;
    }

    @Override
    public void addLand(PlotSection section) {

    }

    public static Settlement tryCreate(String name, User leader, boolean silent) {
        if(COMMUNITIES == null) {
            COMMUNITIES = NationsPlugin.getInstance().communitiesRegistry();
        }

        if(COMMUNITIES.get(name) != null) {
            return null;
        }

        if(leader.getCommunity() != null) {
            return null;
        }

        if(LAND.getClaimedChunkAtLocation(leader.getPlayer().getLocation()) != null) {
            return null;
        }

        Settlement s = new Settlement(name, leader);

        ClaimedChunk homeBlock = LAND.tryClaimFullChunkOtherwiseFail(leader.getPlayer().getChunk(), s);

        COMMUNITIES.set(name, s);

        leader.setCommunity(s, silent);
        NationsPlugin.getInstance().permissionManager().registerHolder(s, null);

        return s;
    }
}
