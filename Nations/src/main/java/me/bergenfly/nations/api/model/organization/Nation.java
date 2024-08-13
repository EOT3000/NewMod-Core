package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.model.plot.PlotSection;

import java.util.Set;
import java.util.stream.Stream;

public interface Nation extends LandAdministrator, PlayerGroup, Named, Led {
    Set<PlotSection> getNationLand();

    Set<PlotSection> getSettlementLand();

    Set<Settlement> getSettlements();

    Settlement getCapital();


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
}
