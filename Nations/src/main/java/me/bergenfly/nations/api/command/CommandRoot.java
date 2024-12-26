package me.bergenfly.nations.api.command;

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

    public final Pair<CommandFlower, String[]> getFinal(String[] strings) {
        CommandStem stem = root;

        int count = 0;

        for(String string : strings) {
            CommandStem next = stem.next(string);

            if(next == null) {
                String[] stringsCopy = new String[strings.length-count];

                System.arraycopy(strings, count, stringsCopy, 0, strings.length-count);

                return new ImmutablePair<>(stem.flower, stringsCopy);
            }

            stem = next;

            count++;
        }

        String[] stringsCopy = new String[strings.length-count];

        System.arraycopy(strings, count, stringsCopy, 0, strings.length-count);

        return new ImmutablePair<>(stem.flower, stringsCopy);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Pair<CommandFlower, String[]> result = getFinal(strings);

        return result.getLeft().onCommand(commandSender, command, s, result.getRight(), strings);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Pair<CommandFlower, String[]> result = getFinal(strings);

        CommandFlower left = result.getLeft();

        String last = strings[strings.length-1];

        if(left instanceof HelpCommandFlower h) {
            List<String> ret = new ArrayList<>();

            for(String option : h.stem.branches.keySet()) {
                if(option.startsWith(last)) {
                    ret.add(option);
                }
            }

            if(!ret.isEmpty()) {
                return ret;
            }

            return new ArrayList<>(h.stem.branches.keySet());
        }

        if(result.getRight().length == 0) {
            return new ArrayList<>();
        }

        ArgumentTabCompleter completer = left.tabCompleters.get(result.getRight().length-1);

        if(completer == null) {
            return new ArrayList<>();
        } else {
            return completer.complete(commandSender);
        }
    }
}
