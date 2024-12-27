package me.bergenfly.nations.impl.util;

public class IdUtil {

    public static String settlementId1(String firstName, long creationTime) {
        return "settlement_" + firstName.toLowerCase() + "_" + creationTime;
    }

    public static String nationId1(String firstName, long creationTime) {
        return "nation_" + firstName.toLowerCase() + "_" + creationTime;
    }

    public static String rankId1(String firstName, String nationId, long creationTime) {
        return "rank_" + nationId + "_" + firstName.toLowerCase() + "_" + creationTime;
    }

    public static long creationTimeFromId1(String id) {
        int ind = id.lastIndexOf("_");

        return Long.parseLong(id.substring(ind+1));
    }

    public static String nameFromId1(String id) {
        int ind = id.lastIndexOf("_");

        String p1 = id.substring(0,ind);

        int ind2 = p1.lastIndexOf("_");

        return p1.substring(ind2+1);
    }

    public static void main(String[] args) {
        System.out.println(nameFromId1("dhuuw_NAME_2893"));
    }
}
