package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.registry.Registry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class CommandFlower {

    protected static final Pattern pattern = Pattern.compile("^((?!([a-z]|[A-Z]|[0-9]|_)).)*$|__");

    static {
        // When changing change in check method too
        CURRENT_LOCATION = -10;
        MEMBERSHIP = -20;
        SELF = -20;
    }

    public static final int CURRENT_LOCATION;
    public static final int MEMBERSHIP;
    public static final int SELF;

    private static Registry<User, UUID> USERS = null;

    private Predicate<NationsCommandInvocation> command;

    private final IntArrayList nations = new IntArrayList();
    private final IntArrayList settlements = new IntArrayList();
    private final IntArrayList users = new IntArrayList();
    private final IntArrayList ints = new IntArrayList();
    private final IntArrayList floats = new IntArrayList();
    private final IntArrayList booleans = new IntArrayList();

    private final IntArrayList nationsNotExist = new IntArrayList();
    private final IntArrayList settlementsNotExist = new IntArrayList();

    private final IntArrayList cleanName = new IntArrayList();

    private boolean mustBePlayer = false;

    public Function<NationsCommandInvocation, String> successBroadcast = null;
    public Function<NationsCommandInvocation, String> successMessage = null;
    public Function<NationsCommandInvocation, String> failureMessage = null;

    private NationsCommandInvocationMaker maker;

    public CommandFlower() {
    }

    public CommandFlower addNation(int i) {
        nations.add(i);
        return this;
    }
    public CommandFlower addSettlement(int i) {
        settlements.add(i);
        return this;
    }
    public CommandFlower addUser(int i) {
        users.add(i);
        return this;
    }
    public CommandFlower addInt(int i) {
        ints.add(i);
        return this;
    }
    public CommandFlower addFloat(int i) {
        floats.add(i);
        return this;
    }
    public CommandFlower addBoolean(int i) {
        booleans.add(i);
        return this;
    }

    public CommandFlower nationDoesNotExist(int i) {
        nationsNotExist.add(i);
        return this;
    }
    public CommandFlower settlementDoesNotExist(int i) {
        settlementsNotExist.add(i);
        return this;
    }

    public CommandFlower cleanName(int i) {
        cleanName.add(i);
        return this;
    }

    public CommandFlower command(Predicate<NationsCommandInvocation> command) {
        this.command = command;
        return this;
    }

    public CommandFlower successBroadcast(Function<NationsCommandInvocation, String> function) {
        this.successBroadcast = function;
        return this;
    }
    public CommandFlower successMessage(Function<NationsCommandInvocation, String> function) {
        this.successMessage = function;
        return this;
    }
    public CommandFlower failureMessage(Function<NationsCommandInvocation, String> function) {
        this.failureMessage = function;
        return this;
    }

    public CommandFlower player() {
        this.mustBePlayer = true;
        return this;
    }

    public CommandFlower make() {
        maker = new NationsCommandInvocationMaker(nations, settlements, users, ints, floats, booleans);
        return this;
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        System.out.println(Arrays.toString(strings));

        NationsCommandInvocation made = maker.make((Player) commandSender, strings);

        if(!made.valid) {
            return false;
        }

        if(this.command.test(made)) {
            String send = successBroadcast.apply(made);

            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(send);
            }

            commandSender.sendMessage(successMessage.apply(made));
            return true;
        } else {
            commandSender.sendMessage(failureMessage.apply(made));
            return false;
        }
    }

    private class NationsCommandInvocationMaker {
        private BiFunction<Player, String[], Nation[]> nations = (a, b) -> null;
        private BiFunction<Player, String[], Settlement[]> settlements = (a, b) -> null;
        private BiFunction<Player, String[], User[]> users = (a, b) -> null;
        private BiFunction<Player, String[], int[]> ints = (a, b) -> null;
        private BiFunction<Player, String[], float[]> floats = (a, b) -> null;
        private BiFunction<Player, String[], boolean[]> booleans = (a, b) -> null;

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

            // If still valid, then check the NOT requirements
            valid = valid && RequirementCheckers.checkSettlementsNotExist(settlementsNotExist, player, strings) && RequirementCheckers.checkNationsNotExist(nationsNotExist, player, strings);

            return new NationsCommandInvocation(player, USERS.get(player.getUniqueId()), strings, valid, nations, settlements, users, ints, floats, booleans);
        }
    }

    public static final record NationsCommandInvocation(@NotNull CommandSender invoker, @Nullable User invokerUser, @NotNull String[] args, boolean valid, Nation[] nations, Settlement[] settlements, User[] users, int[] ints, float[] floats, boolean[] booleans) {}

}
