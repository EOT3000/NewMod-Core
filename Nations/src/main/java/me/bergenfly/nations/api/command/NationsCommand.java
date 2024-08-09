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

    private static Registry<Nation, String> NATIONS = null;

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

    private class NationsCommandInvocationBuilder {
        private BiFunction<Player, String[], Nation[]> nations = (_, _) -> null;
        private BiFunction<Player, String[], Settlement[]> settlements = (_, _) -> null;
        private BiFunction<Player, String[], User[]> users = (_, _) -> null;
        private BiFunction<Player, String[], int[]> ints = (_, _) -> null;
        private BiFunction<Player, String[], float[]> floats = (_, _) -> null;
        private BiFunction<Player, String[], boolean[]> booleans = (_, _) -> null;

        private NationsCommandInvocationBuilder(IntArrayList nations_, IntArrayList settlements_, IntArrayList users_, IntArrayList ints_, IntArrayList floats_, IntArrayList booleans_) {
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
    }

    public static final record NationsCommandInvocation(@NotNull Player invoker, @NotNull String[] args, Nation[] nations, Settlement[] settlements, User[] users, int[] ints, float[] floats, boolean[] booleans) {

    }

}
