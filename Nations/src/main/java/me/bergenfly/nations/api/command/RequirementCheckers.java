package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntList;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Community;
import me.bergenfly.nations.api.model.organization.Company;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.permission.NationPermission;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.regex.Pattern;

public class RequirementCheckers {
    private static int CURRENT_LOCATION = CommandFlower.CURRENT_LOCATION;
    private static int INVOKER_MEMBER = CommandFlower.INVOKER_MEMBER;
    private static int INVOKER_LEADER = CommandFlower.INVOKER_LEADER;
    private static int SELF = CommandFlower.SELF;

    protected static final Pattern pattern = Pattern.compile("[^a-zA-Z\\d_]|__");

    private static Registry<Nation, String> NATIONS = NationsPlugin.getInstance().nationsRegistry();
    private static Registry<Community, String> COMMUNITIES = NationsPlugin.getInstance().communitiesRegistry();
    private static Registry<User, UUID> USERS = NationsPlugin.getInstance().usersRegistry();
    private static Registry<Company, String> COMPANIES = NationsPlugin.getInstance().companiesRegistry();

    public static boolean checkNationNotExist(String[] args, int i, CommandSender sender) {
        Player player = (Player) sender;

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
            if(i >= args.length) {
                sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                return false;
            }

            Nation nation = NATIONS.get(args[i]);

            if(nation != null) {
                sender.sendMessage(TranslatableString.translate("nations.command.error.nation.is_argument", nation.getName()));
                return false;
            }
        }

        return true;
    }

    public static boolean checkCompanyNotExist(String[] args, int i, CommandSender sender) {
        if (i >= args.length) {
            sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

            return false;
        }

        Company company = COMPANIES.get(args[i]);

        if (company != null) {
            sender.sendMessage(TranslatableString.translate("nations.command.error.company.is_argument", company.getName()));
            return false;
        }

        return true;
    }

    public static boolean checkCommunityNotExist(String[] args, int i, CommandSender sender) {
        Player player = (Player) sender;

        if(i == INVOKER_MEMBER) {
            User user = USERS.get(player.getUniqueId());

            if(user.getCommunity() != null) {
                sender.sendMessage(TranslatableString.translate("nations.command.error.community.is_member"));
                return false;
            }
        } else if(i == INVOKER_LEADER) {
            User user = USERS.get(player.getUniqueId());

            if(user.getCommunity() != null && user.getCommunity().getLeader() == user) {
                sender.sendMessage(TranslatableString.translate("nations.command.error.user.is_leader", word(user.getCommunity())));
                return false;
            }
        }  else if(i == CURRENT_LOCATION) {
            //TODO
        } else {
            if(i >= args.length) {
                sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                return false;
            }

            Community community = COMMUNITIES.get(args[i]);

            if(community != null) {
                sender.sendMessage(TranslatableString.translate("nations.command.error.settlement.is_argument", community.getName()));
                return false;
            }
        }

        return true;
    }

    public static boolean checkCleanName(String[] args, int i, CommandSender sender) {
        if(i >= args.length) {
            sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

            return false;
        }

        if(pattern.matcher(args[i]).find()) {
            sender.sendMessage(TranslatableString.translate("nations.command.error.non_alphanumeric"), args[i]);

            return false;
        }

        return true;
    }

    public static boolean checkNameLength(String[] args, int i, CommandSender sender, int min, int max) {
        if(i >= args.length) {
            sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

            return false;
        }

        if(args[i].length() < min) {
            sender.sendMessage(TranslatableString.translate("nations.command.error.string.short", args[i], min + "", max + ""));

            return false;
        }

        if(args[i].length() > max) {
            sender.sendMessage(TranslatableString.translate("nations.command.error.string.long", args[i], min + "", max + ""));

            return false;
        }

        return true;
    }

    public static boolean checkNationPermission(CommandSender sender, NationPermission permission) {
        Player player = (Player) sender;

        User user = USERS.get(player.getUniqueId());

        Nation n = user.getNation();

        return n != null && (n.getLeader() == user || n.hasPermission(user, permission));
    }

    public interface RequirementChecker {
        boolean test(String[] args, int i, CommandSender sender);
    }

    public static String word(Community c) {
        return c.isSettlement() ? "settlement" : "tribe";
    }
}
