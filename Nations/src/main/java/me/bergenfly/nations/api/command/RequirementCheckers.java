package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntList;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.registry.Registry;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RequirementCheckers {
    private static int CURRENT_LOCATION = CommandFlower.CURRENT_LOCATION;
    private static int MEMBERSHIP = CommandFlower.MEMBERSHIP;
    private static int SELF = CommandFlower.SELF;

    private static Registry<Nation, String> NATIONS = null;
    private static Registry<Settlement, String> SETTLEMENTS = null;
    private static Registry<User, UUID> USERS = null;

    public static boolean checkNationsNotExist(IntList list, Player player, String[] strings) {
        for(int i : list) {
            if(i == MEMBERSHIP) {
                User user = USERS.get(player.getUniqueId());

                if(user.getNation() != null) {
                    player.sendMessage(TranslatableString.translate("nations.command.error.nation.is_member"));
                    return false;
                }
            } else if(i == CURRENT_LOCATION) {
                //TODO
            } else {
                Nation nation = NATIONS.get(strings[list.getInt(i)]);

                if(nation != null) {
                    player.sendMessage(TranslatableString.translate("nations.command.error.nation.is_argument", nation.getName()));
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean checkSettlementsNotExist(IntList list, Player player, String[] strings) {
        for(int i : list) {
            if(i == MEMBERSHIP) {
                User user = USERS.get(player.getUniqueId());

                if(user.getSettlement() != null) {
                    player.sendMessage(TranslatableString.translate("nations.command.error.settlement.is_member"));
                    return false;
                }
            } else if(i == CURRENT_LOCATION) {
                //TODO
            } else {
                Settlement settlement = SETTLEMENTS.get(strings[list.getInt(i)]);

                if(settlement != null) {
                    player.sendMessage(TranslatableString.translate("nations.command.error.settlement.is_argument", settlement.getName()));
                    return false;
                }
            }
        }

        return true;
    }
}
