package me.bergenfly.nations.config;

import static me.bergenfly.nations.config.DefaultTownPermission.*;

public enum DefaultRankSystem {
    /*
    //RESIDENT MANAGEMENT:

    INVITE_RESIDENT,
    PROPOSE_KICK,

    OUTLAW_PLAYER,

    //ZONING AND LAW ENFORCEMENT

    ISSUE_WARRANT,
    ISSUE_LOCK,
    ISSUE_EVICTION,
    ZONING,
    SELL_TOWN_PLOT,

    CLAIM,
    UNCLAIM,

    //RANKS

    HIRE_HELPER,
    HIRE_DEPUTY,

    //TOWN PROPERTY STUFF:

    ACCESS_ROAD,
    ACCESS_PARK,
    ACCESS_SUBWAY,
    ACCESS_SHERIFF_OFFICE,

    SELL_PUBLIC_APARTMENT,

    VIEW_VAULT,
     */

    HELPER(INVITE_RESIDENT, ACCESS_ROAD, ACCESS_PARK, ACCESS_SUBWAY, MANAGE_PUBLIC_HOUSING, VIEW_VAULT),
    DEPUTY(INVITE_RESIDENT, PROPOSE_KICK, ISSUE_WARRANT, ACCESS_SHERIFF_OFFICE, VIEW_VAULT);

    DefaultRankSystem(DefaultTownPermission... permissions) {

    }
}
