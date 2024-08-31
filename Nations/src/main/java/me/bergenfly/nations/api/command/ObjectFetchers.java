package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.permission.DefaultPlotPermission;
import me.bergenfly.nations.api.permission.PlotPermission;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class ObjectFetchers {
    private static final int CURRENT_LOCATION = CommandFlower.CURRENT_LOCATION;
    private static final int INVOKER_MEMBER = CommandFlower.INVOKER_MEMBER;
    private static final int INVOKER_LEADER = CommandFlower.INVOKER_LEADER;
    private static int SELF = CommandFlower.SELF;

    private static Registry<Nation, String> NATIONS = NationsPlugin.getInstance().nationsRegistry();
    private static Registry<Settlement, String> SETTLEMENTS = NationsPlugin.getInstance().settlementsRegistry();
    private static Registry<User, UUID> USERS = NationsPlugin.getInstance().usersRegistry();
    private static Registry<Map<Class<?>, LandPermissionHolder>, String> PERMISSION_HOLDERS = NationsPlugin.getInstance().permissionHoldersByNameRegistry();

    public static BiFunction<CommandSender, String[], Nation[]> createNationFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            Nation[] r = new Nation[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) == CURRENT_LOCATION) {
                    r[i] = null; //TODO

                    if(r[i] == null) {
                        sender.sendMessage(TranslatableString.translate("nations.command.error.nation.not_in_territory"));
                        return new Nation[0];
                    }
                } else if(list.getInt(i) == INVOKER_MEMBER) {
                    Player player = (Player) sender;
                    r[i] = USERS.get(player.getUniqueId()).getNation();

                    if(r[i] == null) {
                        sender.sendMessage(TranslatableString.translate("nations.command.error.nation.not_member"));
                        return new Nation[0];
                    }
                } else if(list.getInt(i) == INVOKER_LEADER) {
                    Player player = (Player) sender;
                    User user = USERS.get(player.getUniqueId());
                    r[i] = user.getNation();

                    if(r[i] == null || r[i].getLeader() != user) {
                        sender.sendMessage(TranslatableString.translate("nations.command.error.nation.not_leader"));
                        return new Nation[0];
                    }
                } else {
                    if(list.getInt(i) >= strings.length) {
                        sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                        return new Nation[0];
                    }

                    r[i] = NATIONS.get(strings[list.getInt(i)]);

                    if(r[i] == null) {
                        sender.sendMessage(TranslatableString.translate("nations.command.error.nation.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
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
                    r[i] = null; //TODO

                    if(r[i] == null) {
                        sender.sendMessage(TranslatableString.translate("nations.command.error.settlement.not_in_territory"));
                        return new Settlement[0];
                    }
                } else if (list.getInt(i) == INVOKER_MEMBER) {
                    Player player = (Player) sender;
                    r[i] = USERS.get(player.getUniqueId()).getSettlement();

                    if(r[i] == null) {
                        sender.sendMessage(TranslatableString.translate("nations.command.error.settlement.not_member"));
                        return new Settlement[0];
                    }
                }  else if(list.getInt(i) == INVOKER_LEADER) {
                    Player player = (Player) sender;
                    User user = USERS.get(player.getUniqueId());
                    r[i] = user.getSettlement();

                    if(r[i] == null || r[i].getLeader() != user) {
                        sender.sendMessage(TranslatableString.translate("nations.command.error.settlement.not_leader"));
                        return new Settlement[0];
                    }
                } else {
                    if(list.getInt(i) >= strings.length) {
                        sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                        return new Settlement[0];
                    }
                    r[i] = SETTLEMENTS.get(strings[list.getInt(i)]);

                    if(r[i] == null) {
                        sender.sendMessage(TranslatableString.translate("nations.command.error.settlement.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
                        return new Settlement[0];
                    }
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
                        sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                        return new User[0];
                    }
                    Player p = Bukkit.getPlayer(strings[list.getInt(i)]);

                    if(p == null) {
                        sender.sendMessage(TranslatableString.translate("nations.command.error.user.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
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
                    sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                    return new LandPermissionHolder[0];
                }

                Collection<LandPermissionHolder> m = PERMISSION_HOLDERS.get(strings[list.getInt(i)]).values();

                if (m.isEmpty()) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.general.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
                    return new LandPermissionHolder[0];
                }

                r[i] = m.iterator().next();
            }

            return r;
        };
    }

    public static BiFunction<CommandSender, String[], PlotPermission[]> createPlotPermissionFetcher(IntArrayList list) {
        int len = list.size();

        return (sender, strings) -> {
            PlotPermission[] r = new PlotPermission[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) >= strings.length) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                    return new PlotPermission[0];
                }

                try {
                    r[i] = DefaultPlotPermission.valueOf(strings[list.getInt(i)].toUpperCase());
                } catch(IllegalArgumentException e) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.permission.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
                    return new PlotPermission[0];
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
                    sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                    return new int[0];
                }
                try {
                    r[i] = (int) Float.parseFloat(strings[list.getInt(i)]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.number.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
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
                    sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                    return new float[0];
                }
                try {
                    r[i] = Float.parseFloat(strings[list.getInt(i)]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.number.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
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
                    sender.sendMessage(TranslatableString.translate("nations.command.error.arguments.lack"));

                    return new boolean[0];
                }

                if(strings[list.getInt(i)].equalsIgnoreCase("true")) {
                    r[i] = true;
                } else if(strings[list.getInt(i)].equalsIgnoreCase("false")) {
                    r[i] = false;
                } else {
                    sender.sendMessage(TranslatableString.translate("nations.command.error.boolean.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
                    return new boolean[0];
                }
            }

            return r;
        };
    }

}
