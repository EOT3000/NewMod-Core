package me.bergenfly.nations.command;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import me.bergenfly.nations.api.manager.NationsPermissionManager;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.*;
import me.bergenfly.nations.api.model.plot.PlotSection;
import me.bergenfly.nations.api.permission.DefaultNationPermission;
import me.bergenfly.nations.api.permission.DefaultPlotPermission;
import me.bergenfly.nations.api.permission.NationPermission;
import me.bergenfly.nations.api.permission.PlotPermission;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.BiFunction;

import static me.bergenfly.nations.api.manager.NationsPermissionManager.*;

import static me.bergenfly.nations.api.command.TranslatableString.translate;


//TODO: this should definitely not be static
public class ObjectFetchers {
    private static final int CURRENT_LOCATION = CommandFlower.CURRENT_LOCATION;
    private static final int INVOKER_MEMBER = CommandFlower.INVOKER_MEMBER;
    private static final int INVOKER_LEADER = CommandFlower.INVOKER_LEADER;
    private static int SELF = CommandFlower.SELF;

    private static Registry<Nation, String> NATIONS = NationsPlugin.getInstance().nationsRegistry();
    private static Registry<Community, String> COMMUNITIES = NationsPlugin.getInstance().communitiesRegistry();
    private static Registry<User, UUID> USERS = NationsPlugin.getInstance().usersRegistry();
    private static NationsPermissionManager PERMISSION_MANAGER = NationsPlugin.getInstance().permissionManager();
    private static Registry<Company, String> COMPANIES = NationsPlugin.getInstance().companiesRegistry();


    public static BiFunction<CommandSender, String[], Nation[]> createNationFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            Nation[] r = new Nation[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) == CURRENT_LOCATION) {
                    r[i] = null; //TODO

                    if(r[i] == null) {
                        sender.sendMessage(translate("nations.command.error.nation.not_in_territory"));
                        return new Nation[0];
                    }
                } else if(list.getInt(i) == INVOKER_MEMBER) {
                    Player player = (Player) sender;
                    r[i] = USERS.get(player.getUniqueId()).getNation();

                    if(r[i] == null) {
                        sender.sendMessage(translate("nations.command.error.nation.not_member"));
                        return new Nation[0];
                    }
                } else if(list.getInt(i) == INVOKER_LEADER) {
                    Player player = (Player) sender;
                    User user = USERS.get(player.getUniqueId());
                    r[i] = user.getNation();

                    if(r[i] == null || r[i].getLeader() != user) {
                        sender.sendMessage(translate("nations.command.error.nation.not_leader"));
                        return new Nation[0];
                    }
                } else {
                    if(list.getInt(i) >= strings.length) {
                        sender.sendMessage(translate("nations.command.error.arguments.lack"));

                        return new Nation[0];
                    }

                    r[i] = NATIONS.get(strings[list.getInt(i)]);

                    if(r[i] == null) {
                        sender.sendMessage(translate("nations.command.error.nation.not_argument", Integer.toString(i+1), strings[list.getInt(i)]));
                        return new Nation[0];
                    }
                }
            }

            return r;
        };
    }

    public static BiFunction<CommandSender, String[], Settlement[]> createSettlementFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            Settlement[] r = new Settlement[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) == CURRENT_LOCATION) {
                    PlotSection ca = USERS.get(((Player) sender).getUniqueId()).currentlyAt();

                    if(ca.getAdministrator() instanceof Settlement) {
                        r[i] = (Settlement) ca.getAdministrator();
                    } else {
                        sender.sendMessage(translate("nations.command.error.settlement.not_in_territory"));
                        return new Settlement[0];
                    }
                } else if (list.getInt(i) == INVOKER_MEMBER) {
                    Player player = (Player) sender;

                    Community c = USERS.get(player.getUniqueId()).getCommunity();

                    if(c instanceof Settlement s) {
                        r[i] = s;
                    } else {
                        sender.sendMessage(translate("nations.command.error.settlement.not_member"));
                        return new Settlement[0];
                    }
                }  else if(list.getInt(i) == INVOKER_LEADER) {
                    Player player = (Player) sender;
                    User user = USERS.get(player.getUniqueId());

                    Community c = user.getCommunity();

                    if(c instanceof Settlement s && s.getLeader() == user) {
                        r[i] = s;
                    } else {
                        sender.sendMessage(translate("nations.command.error.settlement.not_leader"));
                        return new Settlement[0];
                    }
                } else {
                    if(list.getInt(i) >= strings.length) {
                        sender.sendMessage(translate("nations.command.error.arguments.lack"));

                        return new Settlement[0];
                    }
                    Community c = COMMUNITIES.get(strings[list.getInt(i)]);

                    if(c instanceof Settlement s) {
                        r[i] = s;
                    } else {
                        sender.sendMessage(translate("nations.command.error.settlement.not_argument", Integer.toString(i+1), strings[list.getInt(i)]));
                        return new Settlement[0];
                    }
                }
            }

            return r;
        };
    }

    public static BiFunction<CommandSender, String[], Community[]> createCommunityFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            Community[] r = new Community[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) == CURRENT_LOCATION) {
                    r[i] = null; //TODO

                    if(r[i] == null) {
                        sender.sendMessage(translate("nations.command.error.community.not_in_territory"));
                        return new Community[0];
                    }
                } else if (list.getInt(i) == INVOKER_MEMBER) {
                    Player player = (Player) sender;
                    r[i] = USERS.get(player.getUniqueId()).getCommunity();

                    if(r[i] == null) {
                        sender.sendMessage(translate("nations.command.error.community.not_member"));
                        return new Community[0];
                    }
                }  else if(list.getInt(i) == INVOKER_LEADER) {
                    Player player = (Player) sender;
                    User user = USERS.get(player.getUniqueId());
                    r[i] = user.getCommunity();

                    if(r[i] == null || r[i].getLeader() != user) {
                        sender.sendMessage(translate("nations.command.error.community.not_leader"));
                        return new Community[0];
                    }
                } else {
                    if(list.getInt(i) >= strings.length) {
                        sender.sendMessage(translate("nations.command.error.arguments.lack"));

                        return new Community[0];
                    }
                    r[i] = COMMUNITIES.get(strings[list.getInt(i)]);

                    if(r[i] == null) {
                        sender.sendMessage(translate("nations.command.error.community.not_argument", Integer.toString(i+1), strings[list.getInt(i)]));
                        return new Community[0];
                    }
                }
            }

            return r;
        };
    }

    public static BiFunction<CommandSender, String[], Company[]> createCompanyFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            Company[] r = new Company[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) >= strings.length) {
                    sender.sendMessage(translate("nations.command.error.arguments.lack"));

                    return new Company[0];
                }
                r[i] = COMPANIES.get(strings[list.getInt(i)]);

                if (r[i] == null) {
                    sender.sendMessage(translate("nations.command.error.company.not_argument", Integer.toString(i + 1), strings[list.getInt(i)]));
                    return new Company[0];
                }
            }

            return r;
        };
    }


    public static BiFunction<CommandSender, String[], User[]> createUserFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            User[] r = new User[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) == SELF) {
                    Player player = (Player) sender;
                    r[i] = USERS.get(player.getUniqueId());
                } else {
                    if(list.getInt(i) >= strings.length) {
                        sender.sendMessage(translate("nations.command.error.arguments.lack"));

                        return new User[0];
                    }
                    Player p = Bukkit.getPlayer(strings[list.getInt(i)]);

                    if(p == null) {
                        sender.sendMessage(translate("nations.command.error.user.not_argument", Integer.toString(i+1), strings[list.getInt(i)]));
                        return new User[0];
                    }

                    r[i] = USERS.get(p.getUniqueId());
                }
            }

            return r;
        };
    }

    public static BiFunction<CommandSender, String[], LandPermissionHolder[]> createPermissionHolderFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            LandPermissionHolder[] r = new LandPermissionHolder[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) >= strings.length) {
                    sender.sendMessage(translate("nations.command.error.arguments.lack"));

                    return new LandPermissionHolder[0];
                }

                IntObjectPair<LandPermissionHolder> m = PERMISSION_MANAGER.get(strings[list.getInt(i)]);

                int c = m.keyInt();

                /*public static final int VALID;
                public static final int INVALID_TYPE;
                public static final int INVALID_NATION;
                public static final int NONE_MATCH_SPECIFIC;
                public static final int NONE_MATCH_GENERAL;
                public static final int MULTIPLE_MATCH;
                public static final int TOO_MANY_PARAMETERS;*/

                if(c == VALID) {
                    r[i] = m.right();
                    continue;
                } else {
                    sendErrorMessage(c, sender, strings[list.getInt(i)]);
                }

                return new LandPermissionHolder[0];
            }

            return r;
        };
    }

    public static void sendErrorMessage(int c, CommandSender sender, String full) {
        if(c == INVALID_TYPE) {
            sender.sendMessage(translate("nations.command.error.holder.invalid_type", full.split(":")[0]));
        } else if(c == INVALID_NATION) {
            sender.sendMessage(translate("nations.command.error.holder.invalid_nation", full.split(":")[1]));
        } else if(c == NONE_MATCH_SPECIFIC) {
            sender.sendMessage(translate("nations.command.error.holder.none_match_specific", full));
        } else if(c == NONE_MATCH_GENERAL) {
            sender.sendMessage(translate("nations.command.error.holder.none_match_general", full));
        } else if(c == MULTIPLE_MATCH) {
            sender.sendMessage(translate("nations.command.error.holder.multiple_match", full));
        } else if(c == TOO_MANY_PARAMETERS) {
            sender.sendMessage(translate("nations.command.error.holder.none_match_specific"));
        }
    }

    public static BiFunction<CommandSender, String[], PlotPermission[]> createPlotPermissionFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            PlotPermission[] r = new PlotPermission[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) >= strings.length) {
                    sender.sendMessage(translate("nations.command.error.arguments.lack"));

                    return new PlotPermission[0];
                }

                try {
                    r[i] = DefaultPlotPermission.valueOf(strings[list.getInt(i)].toUpperCase());
                } catch(IllegalArgumentException e) {
                    sender.sendMessage(translate("nations.command.error.plot_permission.not_argument", Integer.toString(i+1), strings[list.getInt(i)]));
                    return new PlotPermission[0];
                }
            }

            return r;
        };
    }

    public static BiFunction<CommandSender, String[], NationPermission[]> createNationPermissionFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            NationPermission[] r = new NationPermission[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) >= strings.length) {
                    sender.sendMessage(translate("nations.command.error.arguments.lack"));

                    return new NationPermission[0];
                }

                try {
                    r[i] = DefaultNationPermission.valueOf(strings[list.getInt(i)].toUpperCase());
                } catch(IllegalArgumentException e) {
                    sender.sendMessage(translate("nations.command.error.nation_permission.not_argument", Integer.toString(i+1), strings[list.getInt(i)]));
                    return new NationPermission[0];
                }
            }

            return r;
        };
    }

    public static BiFunction<CommandSender, String[], int[]> createIntFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            int[] r = new int[len];

            for (int i = 0; i < len; i++) {
                if(list.getInt(i) >= strings.length) {
                    sender.sendMessage(translate("nations.command.error.arguments.lack"));

                    return new int[0];
                }
                try {
                    r[i] = (int) Float.parseFloat(strings[list.getInt(i)]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(translate("nations.command.error.number.not_argument", Integer.toString(i+1), strings[list.getInt(i)]));
                    return new int[0];
                }

            }

            return r;
        };
    }

    public static BiFunction<CommandSender, String[], float[]> createFloatFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            float[] r = new float[len];

            for (int i = 0; i < len; i++) {
                if(list.getInt(i) >= strings.length) {
                    sender.sendMessage(translate("nations.command.error.arguments.lack"));

                    return new float[0];
                }
                try {
                    r[i] = Float.parseFloat(strings[list.getInt(i)]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(translate("nations.command.error.number.not_argument", Integer.toString(i+1), strings[list.getInt(i)]));
                    return new float[0];
                }

            }

            return r;
        };
    }

    public static BiFunction<CommandSender, String[], boolean[]> createBooleanFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            boolean[] r = new boolean[len];

            for (int i = 0; i < len; i++) {
                if(list.getInt(i) >= strings.length) {
                    sender.sendMessage(translate("nations.command.error.arguments.lack"));

                    return new boolean[0];
                }

                if(strings[list.getInt(i)].equalsIgnoreCase("true")) {
                    r[i] = true;
                } else if(strings[list.getInt(i)].equalsIgnoreCase("false")) {
                    r[i] = false;
                } else {
                    sender.sendMessage(translate("nations.command.error.boolean.not_argument", Integer.toString(i+1), strings[list.getInt(i)]));
                    return new boolean[0];
                }
            }

            return r;
        };
    }

}
