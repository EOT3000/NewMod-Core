package me.bergenfly.nations.config;

public enum DefaultTownPermission implements TownPermission {
    //RESIDENT MANAGEMENT:

    INVITE_RESIDENT,
    PROPOSE_KICK,

    OUTLAW_PLAYER,

    //ZONING AND LAW ENFORCEMENT

    ISSUE_WARRANT,
    ISSUE_EVICTION,
    ZONING,
    SELL_TOWN_PLOTS,

    CLAIM,
    UNCLAIM,

    //RANKS

    HIRE_HELPERS,
    HIRE_DEPUTIES,

    //TOWN PROPERTY STUFF:

    ACCESS_ROAD,
    ACCESS_PARK,
    ACCESS_SUBWAY,
    ACCESS_SHERIFF_OFFICE,

    MANAGE_PUBLIC_HOUSING,

    VIEW_VAULT,
}
