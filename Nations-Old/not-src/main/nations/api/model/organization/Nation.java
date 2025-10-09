package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.permission.NationPermission;

import java.util.Set;

public interface Nation extends Charterer, Organization, LandAdministrator, LandPermissionHolder, PlayerGroup, Named, Led {
    Set<PlotSection> getNationLand();

    Set<PlotSection> getSettlementLand();

    Set<Settlement> getSettlements();

    Set<Tribe> getTribes();

    Set<Community> getCommunities();

    Settlement getCapital();

    Set<Rank> getRanks();

    Rank getRank(String name);

    void addRank(Rank rank);

    void removeRank(Rank rank);

    boolean hasRankWithName(String name);

    boolean hasPermission(User user, NationPermission permission);

    /**
     * Should not be used, except by inheritors of {@link Community}.
     *
     * @deprecated use {@link Community#setNation}
     */
    @Deprecated
    void addCommunity(Community settlement);


    /**
     * Should not be used, except by inheritors of {@link Community}.
     *
     * @deprecated use {@link Community#setNation}
     */
    @Deprecated
    void removeCommunity(Community settlement);

    long getCreationTime();

    String getFirstName();

    void addInvitation(Community settlement);

    Set<Community> getInvitations();
}
