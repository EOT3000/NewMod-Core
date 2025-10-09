package me.bergenfly.nations.model;

import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.manager.NationsLandManager;
import me.bergenfly.nations.model.plot.ClaimedChunk;
import me.bergenfly.nations.model.plot.PlotSection;
import me.bergenfly.nations.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class Settlement implements LandAdministrator {
    private static Registry<Settlement, String> COMMUNITIES;
    private static NationsLandManager LAND;

    private User leader;
    private String name;

    private ClaimedChunk homePlot;

    private Settlement(String name, User leader) {
        this.name = name;
        this.leader = leader;
    }

    public static Settlement tryCreate(String name, User leader) {
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

        leader.setCommunity(s);
        NationsPlugin.getInstance().permissionManager().registerHolder(s, null);

        return s;
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
}
