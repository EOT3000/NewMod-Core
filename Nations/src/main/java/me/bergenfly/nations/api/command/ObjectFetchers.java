package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.registry.Registry;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class ObjectFetchers {
    private static int CURRENT_LOCATION = NationsCommand.CURRENT_LOCATION;
    private static int MEMBERSHIP = NationsCommand.MEMBERSHIP;

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
                        player.sendMessage(TranslatableString.translate("nations.command.error.location.nation"));
                    }
                } else if (list.getInt(i) == MEMBERSHIP) {
                    r[i] = USERS.get(player.getUniqueId()).getNation();

                    if(r[i] == null) {
                        player.sendMessage(TranslatableString.translate("nations.command.error.membership.nation"));
                    }
                } else {
                    r[i] = NATIONS.get(strings[list.getInt(i)]);

                    if(r[i] == null) {
                        player.sendMessage(TranslatableString.translate("nations.command.error.argument.nation"));
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
                        player.sendMessage(TranslatableString.translate("nations.command.error.location.settlement"));
                    }
                } else if (list.getInt(i) == MEMBERSHIP) {
                    r[i] = USERS.get(player.getUniqueId()).getSettlement();

                    if(r[i] == null) {
                        player.sendMessage(TranslatableString.translate("nations.command.error.membership.settlement"));
                    }
                } else {
                    r[i] = SETTLEMENTS.get(strings[list.getInt(i)]);

                    if(r[i] == null) {
                        player.sendMessage(TranslatableString.translate("nations.command.error.argument.settlement"));
                    }
                }
            }

            return r;
        };
    }
}
