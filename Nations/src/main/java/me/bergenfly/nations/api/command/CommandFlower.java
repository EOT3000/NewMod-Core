package me.bergenfly.nations.api.command;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import me.bergenfly.nations.api.manager.NationsLandManager;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Community;
import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.permission.DefaultNationPermission;
import me.bergenfly.nations.api.permission.DefaultPlotPermission;
import me.bergenfly.nations.api.permission.NationPermission;
import me.bergenfly.nations.api.permission.PlotPermission;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import org.apache.commons.lang3.function.TriFunction;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandFlower {

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

    private static final Logger logger = NationsPlugin.getInstance().getLogger();

    private static Registry<User, UUID> USERS = NationsPlugin.getInstance().usersRegistry();
    private static Registry<Nation, String> NATIONS = NationsPlugin.getInstance().nationsRegistry();
    private static Registry<Community, String> COMMUNITIES = NationsPlugin.getInstance().communitiesRegistry();

    private Predicate<NationsCommandInvocation> command;

    private final IntArrayList nations = new IntArrayList();
    private final IntArrayList settlements = new IntArrayList();
    private final IntArrayList communities = new IntArrayList();
    private final IntArrayList users = new IntArrayList();
    private final IntArrayList permissionHolders = new IntArrayList();
    private final IntArrayList plotPermissions = new IntArrayList();
    private final IntArrayList nationPermissions = new IntArrayList();
    private final IntArrayList ints = new IntArrayList();
    private final IntArrayList floats = new IntArrayList();
    private final IntArrayList booleans = new IntArrayList();

    final Int2ObjectArrayMap<ArgumentTabCompleter> tabCompleters = new Int2ObjectArrayMap<>();

    private final List<IntObjectImmutablePair<RequirementCheckers.RequirementChecker>> requirements = new ArrayList<>();

    private boolean mustBePlayer = false;

    public Function<NationsCommandInvocation, String> successBroadcast = null;
    public Function<NationsCommandInvocation, String> successMessage = null;
    public Function<NationsCommandInvocation, String> failureMessage = null;

    private NationsCommandInvocationMaker maker;

    public CommandFlower() {
    }

    public CommandFlower addNation(int i) {
        nations.add(i);
        tabCompleters.put(i, (a) -> NATIONS.list().stream().map(Nation::getName).toList());
        return this;
    }
    public CommandFlower addSettlement(int i) {
        settlements.add(i);
        tabCompleters.put(i, (a) -> COMMUNITIES.list().stream().filter(Community::isSettlement).map(Community::getName).toList());
        return this;
    }
    public CommandFlower addCommunity(int i) {
        communities.add(i);
        tabCompleters.put(i, (a) -> COMMUNITIES.list().stream().map(Community::getName).toList());
        return this;
    }
    public CommandFlower addUser(int i) {
        users.add(i);
        tabCompleters.put(i, (a) -> USERS.list().stream().map(User::getName).toList());
        return this;
    }
    public CommandFlower addPermissionHolder(int i) {
        permissionHolders.add(i);
        return this;
    }
    public CommandFlower addPlotPermission(int i) {
        plotPermissions.add(i);
        tabCompleters.put(i, (a) -> Arrays.stream(DefaultPlotPermission.values()).map(PlotPermission::getName).toList());
        return this;
    }
    public CommandFlower addNationPermission(int i) {
        nationPermissions.add(i);
        tabCompleters.put(i, (a) -> Arrays.stream(DefaultNationPermission.values()).map(NationPermission::getName).toList());
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
        tabCompleters.put(i, (a) -> Arrays.asList("true", "false"));
        return this;
    }

    public CommandFlower nationDoesNotExist(int i) {
        requirements.add(new IntObjectImmutablePair<>(i, RequirementCheckers::checkNationNotExist));
        return this;
    }
    public CommandFlower settlementDoesNotExist(int i) {
        requirements.add(new IntObjectImmutablePair<>(i, RequirementCheckers::checkSettlementNotExist));
        return this;
    }

    public CommandFlower cleanName(int i) {
        requirements.add(new IntObjectImmutablePair<>(i, RequirementCheckers::checkCleanName));
        return this;
    }

    public CommandFlower nameLength(int i, int min, int max) {
        requirements.add(new IntObjectImmutablePair<>(i, (a,b,c) -> RequirementCheckers.checkNameLength(a,b,c,min,max)));
        return this;
    }

    public CommandFlower nationPermission(NationPermission permission) {
        requirements.add(new IntObjectImmutablePair<>(0, (a,b,c) -> RequirementCheckers.checkNationPermission(c,permission)));
        return this;
    }

    public CommandFlower argsLength(int length) {
        requirements.add(new IntObjectImmutablePair<>(0, (a,b,c) -> a.length >= length));
        return this;
    }

    public CommandFlower requirement(int i, RequirementCheckers.RequirementChecker checker, String failureKey) {
        return requirement(i, checker, (a) -> TranslatableString.translate(failureKey));
    }

    public CommandFlower requirement(int i, RequirementCheckers.RequirementChecker checker, Function<String[], String> failure) {
        requirements.add(new IntObjectImmutablePair<>(i, (a,b,c) -> {
            boolean result = checker.test(a,b,c);

            if(!result) {
                c.sendMessage(failure.apply(a));
            }

            return result;
        }));
        return this;
    }

    public CommandFlower requirement(int i, RequirementCheckers.RequirementChecker checker) {
        requirements.add(new IntObjectImmutablePair<>(i, checker));
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
        maker = new NationsCommandInvocationMaker(nations, settlements, communities, users, permissionHolders, plotPermissions, nationPermissions, ints, floats, booleans);
        return this;
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings, String[] initial) {
        System.out.println(Arrays.toString(strings));

        NationsCommandInvocation made = maker.make(commandSender, strings);

        if(!made.valid) {
            return false;
        }

        try {
            if (this.command.test(made)) {
                if (successBroadcast != null) {
                    String send = successBroadcast.apply(made);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(send);
                    }
                }

                if (successMessage != null) commandSender.sendMessage(successMessage.apply(made));
                return true;
            } else {
                if (failureMessage != null) commandSender.sendMessage(failureMessage.apply(made));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            logger.log(Level.WARNING, "Error executing command");
            logger.log(Level.WARNING, "Final arguments: " + Arrays.toString(strings));
            logger.log(Level.WARNING, "All arguments: " + Arrays.toString(initial));
            logger.log(Level.WARNING, "---");

            return false;
        }
    }

    private class NationsCommandInvocationMaker {
        private BiFunction<CommandSender, String[], Nation[]> nations = (a, b) -> null;
        private BiFunction<CommandSender, String[], Settlement[]> settlements = (a, b) -> null;
        private BiFunction<CommandSender, String[], Community[]> communities = (a, b) -> null;
        private BiFunction<CommandSender, String[], User[]> users = (a, b) -> null;
        private BiFunction<CommandSender, String[], LandPermissionHolder[]> permissionHolders = (a, b) -> null;
        private BiFunction<CommandSender, String[], PlotPermission[]> plotPermissions = (a, b) -> null;
        private BiFunction<CommandSender, String[], NationPermission[]> nationPermissions = (a, b) -> null;
        private BiFunction<CommandSender, String[], int[]> ints = (a, b) -> null;
        private BiFunction<CommandSender, String[], float[]> floats = (a, b) -> null;
        private BiFunction<CommandSender, String[], boolean[]> booleans = (a, b) -> null;

        private NationsCommandInvocationMaker(IntArrayList nations_, IntArrayList settlements_, IntArrayList communities_, IntArrayList users_, IntArrayList permissionHolders_,
                                              IntArrayList plotPermissions_, IntArrayList nationPermissions_,
                                              IntArrayList ints_, IntArrayList floats_, IntArrayList booleans_) {
            if(!nations_.isEmpty()) {
                nations = ObjectFetchers.createNationFetcher(nations_);
            }

            if(!settlements_.isEmpty()) {
                settlements = ObjectFetchers.createSettlementFetcher(settlements_);
            }

            if(!communities_.isEmpty()) {
                communities = ObjectFetchers.createCommunityFetcher(communities_);
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

            if(!nationPermissions_.isEmpty()) {
                nationPermissions = ObjectFetchers.createNationPermissionFetcher(nationPermissions_);
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
            Community[] communities = this.communities.apply(player, strings);
            User[] users = this.users.apply(player, strings);
            LandPermissionHolder[] permissionHolders = this.permissionHolders.apply(player, strings);
            PlotPermission[] plotPermissions = this.plotPermissions.apply(player, strings);
            NationPermission[] nationPermissions = this.nationPermissions.apply(player, strings);
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
            } else if (nationPermissions != null && nationPermissions.length == 0) {
                valid = false;
            } else if (ints != null && ints.length == 0) {
                valid = false;
            } else if (floats != null && floats.length != 0) {
                valid = false;
            } else if (booleans != null && booleans.length != 0) {
                valid = false;
            }

            // If still valid, then check the other requirements
            for(IntObjectImmutablePair<RequirementCheckers.RequirementChecker> checker : requirements) {
                if(!valid) {
                    break;
                }

                valid = checker.right().test(strings, checker.leftInt(), sender);
            }

            return new NationsCommandInvocation(sender, mustBePlayer ? USERS.get(player.getUniqueId()) : null, strings, valid, nations, settlements, communities, users, permissionHolders, plotPermissions, nationPermissions, ints, floats, booleans);
        }
    }

    public static final record NationsCommandInvocation(@NotNull CommandSender invoker, @Nullable User invokerUser, @NotNull String[] args, boolean valid, Nation[] nations, Settlement[] settlements, Community[] communities, User[] users, LandPermissionHolder[] permissionHolders, PlotPermission[] plotPermissions, NationPermission[] nationPermissions, int[] ints, float[] floats, boolean[] booleans) {}

}
