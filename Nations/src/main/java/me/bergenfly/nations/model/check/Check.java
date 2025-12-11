package me.bergenfly.nations.model.check;

import me.bergenfly.nations.model.Town;
import me.bergenfly.nations.model.User;

public class Check {
    public static boolean checkResidentCanJoinTown(User user, Town town) {
        if(!user.hasCommunity()) {
            return true;
        } else return user.getCommunity().canRemoveResident(user) && !town.isOutlaw(user);
    }
}
