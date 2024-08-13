package me.bergenfly.nations.api.command;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class CommandRoot implements CommandExecutor {

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

        return result.getLeft().onCommand(commandSender, command, s, result.getRight());
    }
}
