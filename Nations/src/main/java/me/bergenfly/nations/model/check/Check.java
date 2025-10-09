package me.bergenfly.nations.model.check;

import me.bergenfly.nations.model.Settlement;
import me.bergenfly.nations.model.User;

public class Check {
    public static boolean checkResidentCanJoinTown(User user, Settlement settlement) {
        if(!user.hasCommunity()) {
            return true;
        } else return user.getCommunity().canRemoveResident(user);
    }
}
