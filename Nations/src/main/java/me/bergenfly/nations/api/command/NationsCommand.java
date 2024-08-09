package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.registry.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class NationsCommand {

    static {
        // When changing change in check method too
        CURRENT_LOCATION = -10;
        MEMBERSHIP = -20;
        SELF = -20;
    }

    public static final int CURRENT_LOCATION;
    public static final int MEMBERSHIP;
    public static final int SELF;


    private final IntArrayList nations = new IntArrayList();
    private final IntArrayList settlements = new IntArrayList();
    private final IntArrayList users = new IntArrayList();
    private final IntArrayList ints = new IntArrayList();
    private final IntArrayList floats = new IntArrayList();
    private final IntArrayList booleans = new IntArrayList();

    public NationsCommand() {

    }

    public void addNation(int i) {
        nations.add(i);
    }
    public void addSettlement(int i) {
        settlements.add(i);
    }
    public void addUser(int i) {
        users.add(i);
    }
    public void addInt(int i) {
        ints.add(i);
    }
    public void addFloat(int i) {
        floats.add(i);
    }
    public void addBoolean(int i) {
        booleans.add(i);
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


        return false;
    }

    private class NationsCommandInvocationMaker {
        private BiFunction<Player, String[], Nation[]> nations = (_, _) -> null;
        private BiFunction<Player, String[], Settlement[]> settlements = (_, _) -> null;
        private BiFunction<Player, String[], User[]> users = (_, _) -> null;
        private BiFunction<Player, String[], int[]> ints = (_, _) -> null;
        private BiFunction<Player, String[], float[]> floats = (_, _) -> null;
        private BiFunction<Player, String[], boolean[]> booleans = (_, _) -> null;

        private NationsCommandInvocationMaker(IntArrayList nations_, IntArrayList settlements_, IntArrayList users_, IntArrayList ints_, IntArrayList floats_, IntArrayList booleans_) {
            if(!nations_.isEmpty()) {
                nations = ObjectFetchers.createNationFetcher(nations_);
            }

            if(!settlements_.isEmpty()) {
                settlements = ObjectFetchers.createSettlementFetcher(settlements_);
            }

            if(!users_.isEmpty()) {
                users = ObjectFetchers.createUserFetcher(users_);
            }

            if(!ints_.isEmpty()) {
                ints = ObjectFetchers.createIntFetcher(ints_);
            }

            if(!floats_.isEmpty()) {
                floats = ObjectFetchers.createFloatFetcher(floats_);
            }

            if(!booleans_.isEmpty()) {
                booleans = ObjectFetchers.createBooleanFetcher(booleans_);
            }
        }

        private NationsCommandInvocation make(Player player, String[] strings) {
            Nation[] nations = this.nations.apply(player, strings);
            Settlement[] settlements = this.settlements.apply(player, strings);
            User[] users = this.users.apply(player, strings);
            int[] ints = this.ints.apply(player, strings);
            float[] floats = this.floats.apply(player, strings);
            boolean[] booleans = this.booleans.apply(player, strings);

            boolean valid = true;

            //If any of the arrays are length 0, means there was an error, in ObjectFetchers, so mark as invalid. If array is null, just means that
            if(nations != null && nations.length == 0) {
                valid = false;
            } else if(settlements != null && settlements.length == 0) {
                valid = false;
            } else if(users != null && users.length == 0) {
                valid = false;
            } else if(ints != null && ints.length == 0) {
                valid = false;
            } else if(floats != null && floats.length != 0) {
                valid = false;
            } else if(booleans != null && booleans.length != 0) {
                valid = false;
            }

            return new NationsCommandInvocation(player, strings, valid, nations, settlements, users, ints, floats, booleans);
        }
    }

    public static final record NationsCommandInvocation(@NotNull Player invoker, @NotNull String[] args, boolean valid, Nation[] nations, Settlement[] settlements, User[] users, int[] ints, float[] floats, boolean[] booleans) {}

}
