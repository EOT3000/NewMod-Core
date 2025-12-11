package me.bergenfly.nations.command;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CommandRoot implements TabExecutor {

    public abstract void loadSubcommands();

    private final CommandStem root;

    public CommandRoot(String s) {
        this.root = new FirstCommandStem(s);

        loadSubcommands();
    }

    public CommandStem addBranch(String s) {
        return root.addBranch(s);
    }

    public CommandStem addBranch(String s, CommandFlower flower) {
        return root.addBranch(s, flower);
    }

    public final Pair<CommandStem, String[]> getFinalStem(String[] strings) {
        CommandStem stem = root;

        int count = 0;

        for(String string : strings) {
            CommandStem next = stem.next(string);

            if(next == null) {
                String[] stringsCopy = new String[strings.length-count];

                System.arraycopy(strings, count, stringsCopy, 0, strings.length-count);

                return new ImmutablePair<>(stem, stringsCopy);
            }

            stem = next;

            count++;
        }

        String[] stringsCopy = new String[strings.length-count];

        System.arraycopy(strings, count, stringsCopy, 0, strings.length-count);

        return new ImmutablePair<>(stem, stringsCopy);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Pair<CommandStem, String[]> result = getFinalStem(strings);

        return result.getLeft().flower.onCommand(commandSender, command, s, result.getRight(), strings);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Pair<CommandStem, String[]> finalCommandStage = getFinalStem(strings);

        CommandStem finalCommandStageStem = finalCommandStage.getLeft();

        CommandFlower finalCommandStageFlower = finalCommandStage.getLeft().flower;

        String last = strings[strings.length - 1];

        if (finalCommandStage.getRight().length == 0) {
            return new ArrayList<>();
        }

        List<String> ret = new ArrayList<>();

        ArgumentTabCompleter completer = finalCommandStageFlower.tabCompleters.get(finalCommandStage.getRight().length - 1);

        if(completer != null) {
            for (String option : completer.complete(commandSender)) {
                if (option.toLowerCase().startsWith(last.toLowerCase())) {
                    ret.add(option);
                }
            }
        }

        for (String option : finalCommandStageStem.branches.keySet()) {
            if (option.toLowerCase().startsWith(last.toLowerCase())) {
                ret.add(option);
            }
        }

        if (!ret.isEmpty()) {
            return ret;
        }

        return new ArrayList<>(finalCommandStageStem.branches.keySet());
    }
}
