package me.bergenfly.nations.api.command;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CommandStem {

    final Map<String, CommandStem> branches = new HashMap<>();
    public final CommandFlower flower;

    final String key;
    final CommandStem parent;

    protected CommandStem(@Nullable CommandFlower flower, CommandStem parent, String key) {
        this.flower = flower == null ? new HelpCommandFlower(this) : flower;
        this.key = key;
        this.parent = parent;
    }

    public CommandStem addBranch(String s) {
        CommandStem stem = new CommandStem(null, this, s);

        branches.put(s, stem);

        return stem;
    }

    public CommandStem addBranch(String s, CommandFlower flower) {
        CommandStem stem = new CommandStem(flower, this, s);

        branches.put(s, stem);

        return stem;
    }

    public CommandStem next(String strings) {
        return branches.get(strings);
    }
}
