package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntList;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RequirementCheckers {
    private static int CURRENT_LOCATION = CommandFlower.CURRENT_LOCATION;
    private static int INVOKER_MEMBER = CommandFlower.INVOKER_MEMBER;
    private static int INVOKER_LEADER = CommandFlower.INVOKER_LEADER;
    private static int SELF = CommandFlower.SELF;

    private static Registry<Nation, String> NATIONS = NationsPlugin.getInstance().nationsRegistry();
    private static Registry<Settlement, String> SETTLEMENTS = NationsPlugin.getInstance().settlementsRegistry();
    private static Registry<User, UUID> USERS = NationsPlugin.getInstance().usersRegistry();

    public static boolean checkNationsNotExist(IntList list, CommandSender sender, String[] strings) {
        Player player = (Player) sender;

        for(int i : list) {
            if(i == INVOKER_MEMBER) {
                User user = USERS.get(player.getUniqueId());

                if(user.getNation() != null) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.nation.is_member"));
                    return false;
                }
            } else if(i == INVOKER_LEADER) {
                User user = USERS.get(player.getUniqueId());

                if(user.getNation() != null && user.getNation().getLeader() == user) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.nation.is_leader"));
                    return false;
                }
            } else if(i == CURRENT_LOCATION) {
                //TODO
            } else {
                if(i >= strings.length) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                    return false;
                }

                Nation nation = NATIONS.get(strings[i]);

                if(nation != null) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.nation.is_argument", nation.getName()));
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean checkSettlementsNotExist(IntList list, CommandSender sender, String[] strings) {
        Player player = (Player) sender;

        for(int i : list) {
            if(i == INVOKER_MEMBER) {
                User user = USERS.get(player.getUniqueId());

                if(user.getSettlement() != null) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.settlement.is_member"));
                    return false;
                }
            } else if(i == INVOKER_LEADER) {
                User user = USERS.get(player.getUniqueId());

                if(user.getSettlement() != null && user.getSettlement().getLeader() == user) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.nation.is_leader"));
                    return false;
                }
            }  else if(i == CURRENT_LOCATION) {
                //TODO
            } else {
                if(i >= strings.length) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                    return false;
                }

                Settlement settlement = SETTLEMENTS.get(strings[i]);

                if(settlement != null) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.settlement.is_argument", settlement.getName()));
                    return false;
                }
            }
        }

        return true;
    }
}
