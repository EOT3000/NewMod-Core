package me.bergenfly.nations.model.check;

import com.google.common.collect.Sets;
import me.bergenfly.nations.model.Nation;
import me.bergenfly.nations.model.Town;
import me.bergenfly.nations.model.User;

public class Check {
    public static boolean checkResidentCanJoinTown(User user, Town town) {
        if(!user.hasCommunity()) {
            return true;
        } else return user.getCommunity().canRemoveResident(user);
    }

    public static boolean checkTownCanLeaveNation(Town town) {
        if(town.getNation() == null) {
            return true;
        }

        return !town.getNation().getCapital().equals(town);
    }

    public static boolean checkTownCanJoinNation(Town town, Nation nation) {
        return town.getNation() == null || town.getNation() == nation;
    }
}
