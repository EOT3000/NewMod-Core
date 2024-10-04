package me.bergenfly.nations.impl.util;

public class IdUtil {

    public static String settlementId1(String firstName, long creationTime) {
        return "settlement_" + firstName.toLowerCase() + "_" + creationTime;
    }

    public static String nationId1(String firstName, long creationTime) {
        return "nation_" + firstName.toLowerCase() + "_" + creationTime;
    }
}
