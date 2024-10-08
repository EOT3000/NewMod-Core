package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import me.bergenfly.nations.api.manager.NationsLandManager;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.permission.PlotPermission;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class CommandFlower {

    protected static final Pattern pattern = Pattern.compile("^((?!([a-z]|[A-Z]|[0-9]|_)).)*$|__");

    static {
        // When changing change in check method too
        CURRENT_LOCATION = -10;
        INVOKER_MEMBER = -20;
        INVOKER_LEADER = -30;
        SELF = -20;
    }

    public static final int CURRENT_LOCATION;
    public static final int INVOKER_MEMBER;
    public static final int INVOKER_LEADER;
    public static final int SELF;

    private static Registry<User, UUID> USERS = NationsPlugin.getInstance().usersRegistry();

    private Predicate<NationsCommandInvocation> command;

    private final IntArrayList nations = new IntArrayList();
    private final IntArrayList settlements = new IntArrayList();
    private final IntArrayList users = new IntArrayList();
    private final IntArrayList permissionHolders = new IntArrayList();
    private final IntArrayList plotPermissions = new IntArrayList();
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
    public CommandFlower addPermissionHolder(int i) {
        permissionHolders.add(i);
        return this;
    }
    public CommandFlower addPlotPermission(int i) {
        plotPermissions.add(i);
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

    public CommandFlower commandAlwaysSuccess(Consumer<NationsCommandInvocation> command) {
        this.command = (a) -> {
            command.accept(a);
            return true;
        };
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
        maker = new NationsCommandInvocationMaker(nations, settlements, users, permissionHolders, plotPermissions, ints, floats, booleans);
        return this;
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        System.out.println(Arrays.toString(strings));

        NationsCommandInvocation made = maker.make(commandSender, strings);

        if(!made.valid) {
            return false;
        }

        if(this.command.test(made)) {
            if(successBroadcast != null) {
                String send = successBroadcast.apply(made);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(send);
                }
            }

            if(successMessage != null) commandSender.sendMessage(successMessage.apply(made));
            return true;
        } else {
            if(failureMessage != null) commandSender.sendMessage(failureMessage.apply(made));
            return false;
        }
    }

    private class NationsCommandInvocationMaker {
        private BiFunction<CommandSender, String[], Nation[]> nations = (a, b) -> null;
        private BiFunction<CommandSender, String[], Settlement[]> settlements = (a, b) -> null;
        private BiFunction<CommandSender, String[], User[]> users = (a, b) -> null;
        private BiFunction<CommandSender, String[], LandPermissionHolder[]> permissionHolders = (a, b) -> null;
        private BiFunction<CommandSender, String[], PlotPermission[]> plotPermissions = (a, b) -> null;
        private BiFunction<CommandSender, String[], int[]> ints = (a, b) -> null;
        private BiFunction<CommandSender, String[], float[]> floats = (a, b) -> null;
        private BiFunction<CommandSender, String[], boolean[]> booleans = (a, b) -> null;

        private NationsCommandInvocationMaker(IntArrayList nations_, IntArrayList settlements_, IntArrayList users_, IntArrayList permissionHolders_,
                                              IntArrayList plotPermissions_,
                                              IntArrayList ints_, IntArrayList floats_, IntArrayList booleans_) {
            if(!nations_.isEmpty()) {
                nations = ObjectFetchers.createNationFetcher(nations_);
            }

            if(!settlements_.isEmpty()) {
                settlements = ObjectFetchers.createSettlementFetcher(settlements_);
            }

            if(!users_.isEmpty()) {
                users = ObjectFetchers.createUserFetcher(users_);
            }

            if(!permissionHolders_.isEmpty()) {
                permissionHolders = ObjectFetchers.createPermissionHolderFetcher(permissionHolders_);
            }

            if(!plotPermissions_.isEmpty()) {
                plotPermissions = ObjectFetchers.createPlotPermissionFetcher(plotPermissions_);
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

        private NationsCommandInvocation make(CommandSender sender, String[] strings) {
            Player player = null;

            boolean valid = true;

            if (mustBePlayer) {
                player = (Player) sender;
            }

            Nation[] nations = this.nations.apply(player, strings);
            Settlement[] settlements = this.settlements.apply(player, strings);
            User[] users = this.users.apply(player, strings);
            LandPermissionHolder[] permissionHolders = this.permissionHolders.apply(player, strings);
            PlotPermission[] plotPermissions = this.plotPermissions.apply(player, strings);
            int[] ints = this.ints.apply(player, strings);
            float[] floats = this.floats.apply(player, strings);
            boolean[] booleans = this.booleans.apply(player, strings);

            //If any of the arrays are length 0, means there was an error, in ObjectFetchers, so mark as invalid. If array is null, just means that
            if (nations != null && nations.length == 0) {
                valid = false;
            } else if (settlements != null && settlements.length == 0) {
                valid = false;
            } else if (users != null && users.length == 0) {
                valid = false;
            } else if (permissionHolders != null && permissionHolders.length == 0) {
                valid = false;
            } else if (plotPermissions != null && plotPermissions.length == 0) {
                valid = false;
            } else if (ints != null && ints.length == 0) {
                valid = false;
            } else if (floats != null && floats.length != 0) {
                valid = false;
            } else if (booleans != null && booleans.length != 0) {
                valid = false;
            }

            // If still valid, then check the NOT requirements
            valid = valid && RequirementCheckers.checkSettlementsNotExist(settlementsNotExist, sender, strings) && RequirementCheckers.checkNationsNotExist(nationsNotExist, sender, strings);

            return new NationsCommandInvocation(sender, mustBePlayer ? USERS.get(player.getUniqueId()) : null, strings, valid, nations, settlements, users, permissionHolders, plotPermissions, ints, floats, booleans);
        }
    }

    public static final record NationsCommandInvocation(@NotNull CommandSender invoker, @Nullable User invokerUser, @NotNull String[] args, boolean valid, Nation[] nations, Settlement[] settlements, User[] users, LandPermissionHolder[] permissionHolders, PlotPermission[] plotPermissions, int[] ints, float[] floats, boolean[] booleans) {}

}
