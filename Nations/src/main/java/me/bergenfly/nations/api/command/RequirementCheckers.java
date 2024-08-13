package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntList;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RequirementCheckers {
    private static int CURRENT_LOCATION = CommandFlower.CURRENT_LOCATION;
    private static int MEMBERSHIP = CommandFlower.MEMBERSHIP;
    private static int SELF = CommandFlower.SELF;

    private static Registry<Nation, String> NATIONS = NationsPlugin.getInstance().nationsRegistry();
    private static Registry<Settlement, String> SETTLEMENTS = NationsPlugin.getInstance().settlementsRegistry();
    private static Registry<User, UUID> USERS = NationsPlugin.getInstance().usersRegistry();

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
                if(i >= strings.length) {
                    player.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                    return false;
                }

                Nation nation = NATIONS.get(strings[i]);

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
                if(i >= strings.length) {
                    player.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                    return false;
                }

                Settlement settlement = SETTLEMENTS.get(strings[i]);

                if(settlement != null) {
                    player.sendMessage(TranslatableString.translate("nations.command.error.settlement.is_argument", settlement.getName()));
                    return false;
                }
            }
        }

        return true;
    }
}
