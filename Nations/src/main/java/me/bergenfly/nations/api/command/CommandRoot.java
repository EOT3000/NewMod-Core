package me.bergenfly.nations.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class CommandRoot implements CommandExecutor {
    protected int CURRENT_LOCATION = CommandFlower.CURRENT_LOCATION;
    protected int MEMBERSHIP = CommandFlower.MEMBERSHIP;
    protected int SELF = CommandFlower.SELF;

    public abstract void loadSubcommands();

    private static final CommandStem root = new CommandStem(null /*TODO help menu*/);

    public void addBranch(String s, CommandStem branch) {
        root.addBranch(s, branch);
    }

    public final CommandFlower getFinal(String[] strings) {
        CommandStem stem = root;

        for(String string : strings) {
            CommandStem next = stem.next(string);

            if(next == null) {
                return stem.flower;
            }

            stem = next;
        }

        return root.flower;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }
}
