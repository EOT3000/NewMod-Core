package me.bergenfly.nations.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HelpCommandFlower extends CommandFlower {
    private final CommandStem stem;

    public HelpCommandFlower(CommandStem stem) {
        this.stem = stem;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings, String[] a) {
        commandSender.sendMessage("Help");

        for(CommandStem branch : stem.branches.values()) {
            commandSender.sendMessage(buildPrevious(branch));
        }

        commandSender.sendMessage("");

        return false;
    }

    private String buildPrevious(CommandStem which) {
        List<String> list = new ArrayList<>();

        CommandStem current = which;

        do {
            list.add(current.key);

            current = current.parent;

        } while (current != null);

        StringBuilder built = new StringBuilder("/");

        for(int i = 0; i < list.size(); i++) {
            built.append(list.get(list.size()-i-1)).append(" ");
        }

        return built.toString();
    }
}
