package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.registry.Registry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class ObjectFetchers {
    private static int CURRENT_LOCATION = NationsCommand.CURRENT_LOCATION;
    private static int MEMBERSHIP = NationsCommand.MEMBERSHIP;
    private static int SELF = NationsCommand.SELF;

    private static Registry<Nation, String> NATIONS = null;
    private static Registry<Settlement, String> SETTLEMENTS = null;
    private static Registry<User, UUID> USERS = null;

    public static BiFunction<Player, String[], Nation[]> createNationFetcher(IntArrayList list) {
        int len = list.size();

        return (player, strings) -> {
            Nation[] r = new Nation[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) == CURRENT_LOCATION) {
                    r[i] = null; //TODO

                    if(r[i] == null) {
                        player.sendMessage(TranslatableString.translate("nations.command.error.nation.not_in_territory"));
                        return new Nation[0];
                    }
                } else if (list.getInt(i) == MEMBERSHIP) {
                    r[i] = USERS.get(player.getUniqueId()).getNation();

                    if(r[i] == null) {
                        player.sendMessage(TranslatableString.translate("nations.command.error.nation.not_member"));
                        return new Nation[0];
                    }
                } else {
                    r[i] = NATIONS.get(strings[list.getInt(i)]);

                    if(r[i] == null) {
                        player.sendMessage(TranslatableString.translate("nations.command.error.nation.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
                        return new Nation[0];
                    }
                }
            }

            return r;
        };
    }

    public static BiFunction<Player, String[], Settlement[]> createSettlementFetcher(IntArrayList list) {
        int len = list.size();

        return (player, strings) -> {
            Settlement[] r = new Settlement[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) == CURRENT_LOCATION) {
                    r[i] = null; //TODO

                    if(r[i] == null) {
                        player.sendMessage(TranslatableString.translate("nations.command.error.settlement.not_in_territory"));
                        return new Settlement[0];
                    }
                } else if (list.getInt(i) == MEMBERSHIP) {
                    r[i] = USERS.get(player.getUniqueId()).getSettlement();

                    if(r[i] == null) {
                        player.sendMessage(TranslatableString.translate("nations.command.error.settlement.not_member"));
                        return new Settlement[0];
                    }
                } else {
                    r[i] = SETTLEMENTS.get(strings[list.getInt(i)]);

                    if(r[i] == null) {
                        player.sendMessage(TranslatableString.translate("nations.command.error.settlement.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
                        return new Settlement[0];
                    }
                }
            }

            return r;
        };
    }

    public static BiFunction<Player, String[], User[]> createUserFetcher(IntArrayList list) {
        int len = list.size();

        return (player, strings) -> {
            User[] r = new User[len];

            for (int i = 0; i < len; i++) {
                if (list.getInt(i) == SELF) {
                    r[i] = USERS.get(player.getUniqueId());
                } else {
                    Player p = Bukkit.getPlayer(strings[list.getInt(i)]);

                    if(p == null) {
                        player.sendMessage(TranslatableString.translate("nations.command.error.user.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
                        return new User[0];
                    }

                    r[i] = USERS.get(p.getUniqueId());
                }
            }

            return r;
        };
    }

    public static BiFunction<Player, String[], int[]> createIntFetcher(IntArrayList list) {
        int len = list.size();

        return (player, strings) -> {
            int[] r = new int[len];

            for (int i = 0; i < len; i++) {
                try {
                    r[i] = (int) Float.parseFloat(strings[list.getInt(i)]);
                } catch (NumberFormatException e) {
                    player.sendMessage(TranslatableString.translate("nations.command.error.number.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
                    return new int[0];
                }

            }

            return r;
        };
    }

    public static BiFunction<Player, String[], float[]> createFloatFetcher(IntArrayList list) {
        int len = list.size();

        return (player, strings) -> {
            float[] r = new float[len];

            for (int i = 0; i < len; i++) {
                try {
                    r[i] = Float.parseFloat(strings[list.getInt(i)]);
                } catch (NumberFormatException e) {
                    player.sendMessage(TranslatableString.translate("nations.command.error.number.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
                    return new float[0];
                }

            }

            return r;
        };
    }

    public static BiFunction<Player, String[], boolean[]> createBooleanFetcher(IntArrayList list) {
        int len = list.size();

        return (player, strings) -> {
            boolean[] r = new boolean[len];

            for (int i = 0; i < len; i++) {
                if(strings[list.getInt(i)].equalsIgnoreCase("true")) {
                    r[i] = true;
                } else if(strings[list.getInt(i)].equalsIgnoreCase("false")) {
                    r[i] = false;
                } else {
                    player.sendMessage(TranslatableString.translate("nations.command.error.boolean.not_argument", Integer.toString(list.getInt(i)), strings[list.getInt(i)]));
                    return new boolean[0];
                }
            }

            return r;
        };
    }

}
