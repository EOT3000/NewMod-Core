package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.permission.NationPermission;

import java.util.Set;
import java.util.stream.Stream;

public interface Nation extends Charterer, Organization, LandAdministrator, LandPermissionHolder, PlayerGroup, Named, Led {
    Set<PlotSection> getNationLand();

    Set<PlotSection> getSettlementLand();

    Set<Settlement> getSettlements();

    Settlement getCapital();

    Set<Rank> getRanks();

    Rank getRank(String name);

    void addRank(Rank rank);

    void removeRank(Rank rank);

    boolean hasRankWithName(String name);

    boolean hasPermission(User user, NationPermission permission);

    /**
     * Should not be used, except by inheritors of {@link Settlement}.
     *
     * @deprecated use {@link Settlement#setNation}
     */
    @Deprecated
    void addSettlement(Settlement settlement);


    /**
     * Should not be used, except by inheritors of {@link Settlement}.
     *
     * @deprecated use {@link Settlement#setNation}
     */
    @Deprecated
    void removeSettlement(Settlement settlement);

    long getCreationTime();

    String getFirstName();

    void addInvitation(Settlement settlement);

    Set<Settlement> getInvitations();
}
