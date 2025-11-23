package me.bergenfly.nations.command;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.Object2IntFunction;
import me.bergenfly.nations.command.requirement.CommandArgumentType;
import me.bergenfly.nations.command.requirement.CommandRequirement;
import me.bergenfly.nations.manager.NationsLandManager;
import me.bergenfly.nations.model.User;
import me.bergenfly.nations.model.*;
import me.bergenfly.nations.DefaultNationPermission;
import me.bergenfly.nations.DefaultPlotPermission;
import me.bergenfly.nations.permission.NationPermission;
import me.bergenfly.nations.permission.PlotPermission;
import me.bergenfly.nations.registry.Registry;
import me.bergenfly.nations.NationsPlugin;
import org.apache.commons.lang3.function.TriFunction;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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
    private static Registry<Settlement, String> COMMUNITIES = NationsPlugin.getInstance().communitiesRegistry();
    //private static Registry<Company, String> COMPANIES = NationsPlugin.getInstance().companiesRegistry();

    private CommandCompleterWithCodeFunctional command;

    final Int2ObjectArrayMap<ArgumentTabCompleter> tabCompleters = new Int2ObjectArrayMap<>();

    private final Int2ObjectMap<CommandArgumentType<?>> arguments = new Int2ObjectArrayMap<>();
    private final List<CommandRequirement> requirements = new LinkedList<>();

    private Function<NationsCommandInvocation, String> messages = (_) -> null;

    public CommandFlower() {
    }

    public <T> CommandFlower arg(int index, CommandArgumentType<T> type) {
        arguments.put(index, type);

        return this;
    }

    public CommandFlower requirement(CommandRequirement requirement) {
        requirements.add(requirement);

        return this;
    }

    public CommandFlower command(CommandCompleterWithCodeFunctional command) {
        this.command = command;
        return this;
    }

    public CommandFlower commandWithCode(Consumer<NationsCommandInvocation> command, int code) {
        this.command = (a) -> {
            command.accept((NationsCommandInvocation) a);

            return code;
        };
        return this;
    }

    public CommandFlower addMessage(int code, Function<NationsCommandInvocation, String> function) {
        if(function != null) {
            Function<NationsCommandInvocation, String> oldMessages = messages;

            messages = (a) -> {
                if(a.getCode() == code) {
                    return function.apply(a);
                }

                return oldMessages.apply(a);
            };
        }

        return this;
    }



    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings, String[] initial) {
        System.out.println(Arrays.toString(strings));

        for(CommandRequirement requirement : requirements) {
            if(!requirement.passes(commandSender)) {

            }
        }

        try {
            NationsCommandInvocation made = new NationsCommandInvocation(arguments, strings, commandSender);

            try {
                int code = this.command.complete(made);

                return code >= 0;
            } catch (Exception e) {
                e.printStackTrace();

                logger.log(Level.WARNING, "Error executing command");
                logger.log(Level.WARNING, "Final arguments: " + Arrays.toString(strings));
                logger.log(Level.WARNING, "All arguments: " + Arrays.toString(initial));
                logger.log(Level.WARNING, "---");

                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static final class NationsCommandInvocation {
        private Map<CommandArgumentType<?>, List<Object>> converted = new HashMap<>();

        private final String[] args;
        private final CommandSender executor;

        private int code;

        public NationsCommandInvocation(Int2ObjectMap<CommandArgumentType<?>> argumentTypes, String[] rawArguments, CommandSender executor) {
            this.args = rawArguments;
            this.executor = executor;

            for(int i = 0; i < rawArguments.length; i++) {
                String arg = rawArguments[i];
                CommandArgumentType<?> argumentType = argumentTypes.get(i);

                if(argumentType == null) {
                    continue;
                }

                if(argumentType.isValidArgument(arg)) {
                    converted.putIfAbsent(argumentType.getParent(), new ArrayList<>());
                    converted.get(argumentType.getParent()).add(argumentType.convert(arg));
                } else {
                    executor.sendMessage(argumentType.getErrorMessage(executor, arg, i+1));

                    throw new RuntimeException();
                }
            }
        }

        public <T> T getArgument(CommandArgumentType<T> type, int index) {
            return (T) converted.get(type).get(index);
        }

        public CommandSender getInvoker() {
            return executor;
        }

        public Player getInvokerPlayer() throws ClassCastException {
            return (Player) getInvoker();
        }

        public User getInvokerUser() throws ClassCastException {
            return USERS.get(getInvokerPlayer().getUniqueId());
        }

        public int getCode() {
            return code;
        }
    }

}
